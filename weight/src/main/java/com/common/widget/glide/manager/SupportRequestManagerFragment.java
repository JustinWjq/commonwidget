package com.common.widget.glide.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.common.widget.glide.RequestManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A view-less {@link Fragment} used to safely store an
 * {@link RequestManager} that can be used to start, stop and manage Glide requests started for
 * targets within the fragment or activity this fragment is a child of.
 *
 * @see RequestManagerFragment
 * @see RequestManagerRetriever
 * @see RequestManager
 */
public class SupportRequestManagerFragment extends Fragment {

    private static final String TAG = "SupportRMFragment";

    private RequestManager requestManager;
    private final ActivityFragmentLifecycle lifecycle;
    private final RequestManagerTreeNode requestManagerTreeNode =
            new SupportFragmentRequestManagerTreeNode();
    private final HashSet<SupportRequestManagerFragment> childRequestManagerFragments =
        new HashSet<SupportRequestManagerFragment>();
    private com.common.widget.glide.manager.SupportRequestManagerFragment rootRequestManagerFragment;

    public SupportRequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }

    // For testing only.
    @SuppressLint("ValidFragment")
    public SupportRequestManagerFragment(ActivityFragmentLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    /**
     * Sets the current {@link RequestManager}.
     *
     * @param requestManager The manager to set.
     */
    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }


    public ActivityFragmentLifecycle getLifecycle1() {
        return lifecycle;
    }

    /**
     * Returns the current {@link RequestManager} or null if none is set.
     */
    public RequestManager getRequestManager() {
        return requestManager;
    }

    /**
     * Returns the {@link RequestManagerTreeNode} that provides tree traversal methods relative to the associated
     * {@link RequestManager}.
     */
    public RequestManagerTreeNode getRequestManagerTreeNode() {
        return requestManagerTreeNode;
    }

    private void addChildRequestManagerFragment(com.common.widget.glide.manager.SupportRequestManagerFragment child) {
        childRequestManagerFragments.add(child);
    }

    private void removeChildRequestManagerFragment(com.common.widget.glide.manager.SupportRequestManagerFragment child) {
        childRequestManagerFragments.remove(child);
    }

    /**
     * Returns the set of fragments that this RequestManagerFragment's parent is a parent to. (i.e. our parent is
     * the fragment that we are annotating).
     */
    public Set<SupportRequestManagerFragment> getDescendantRequestManagerFragments() {
        if (rootRequestManagerFragment == null) {
            return Collections.emptySet();
        } else if (rootRequestManagerFragment == this) {
            return Collections.unmodifiableSet(childRequestManagerFragments);
        } else {
            HashSet<SupportRequestManagerFragment> descendants =
                new HashSet<SupportRequestManagerFragment>();
            for (com.common.widget.glide.manager.SupportRequestManagerFragment fragment
                : rootRequestManagerFragment.getDescendantRequestManagerFragments()) {
                if (isDescendant(fragment.getParentFragment())) {
                    descendants.add(fragment);
                }
            }
            return Collections.unmodifiableSet(descendants);
        }
    }

    /**
     * Returns true if the fragment is a descendant of our parent.
     */
    private boolean isDescendant(Fragment fragment) {
        Fragment root = this.getParentFragment();
        while (fragment.getParentFragment() != null) {
            if (fragment.getParentFragment() == root) {
                return true;
            }
            fragment = fragment.getParentFragment();
        }
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            rootRequestManagerFragment = RequestManagerRetriever.get()
                    .getSupportRequestManagerFragment(getActivity().getSupportFragmentManager());
            if (rootRequestManagerFragment != this) {
                rootRequestManagerFragment.addChildRequestManagerFragment(this);
            }
        } catch (IllegalStateException e) {
            // OnAttach can be called after the activity is destroyed, see #497.
            if (Log.isLoggable(TAG, Log.WARN)) {
                Log.w(TAG, "Unable to register fragment with root", e);

            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (rootRequestManagerFragment != null) {
            rootRequestManagerFragment.removeChildRequestManagerFragment(this);
            rootRequestManagerFragment = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycle.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycle.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // If an activity is re-created, onLowMemory may be called before a manager is ever set.
        // See #329.
        if (requestManager != null) {
            requestManager.onLowMemory();
        }
    }

    private class SupportFragmentRequestManagerTreeNode implements RequestManagerTreeNode {
        @Override
        public Set<RequestManager> getDescendants() {
            Set<SupportRequestManagerFragment> descendantFragments = getDescendantRequestManagerFragments();
            HashSet<RequestManager> descendants = new HashSet<RequestManager>(descendantFragments.size());
            for (com.common.widget.glide.manager.SupportRequestManagerFragment fragment : descendantFragments) {
                if (fragment.getRequestManager() != null) {
                    descendants.add(fragment.getRequestManager());
                }
            }
            return descendants;
        }
    }
}
