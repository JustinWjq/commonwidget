package com.common.widget.glide.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.util.Log;

import com.common.widget.glide.RequestManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A view-less {@link Fragment} used to safely store an {@link RequestManager} that
 * can be used to start, stop and manage Glide requests started for targets the fragment or activity this fragment is a
 * child of.
 *
 * @see SupportRequestManagerFragment
 * @see RequestManagerRetriever
 * @see RequestManager
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RequestManagerFragment extends Fragment {

    private static final String TAG = "RMFragment";

    private final com.common.widget.glide.manager.ActivityFragmentLifecycle lifecycle;
    private final RequestManagerTreeNode requestManagerTreeNode = new FragmentRequestManagerTreeNode();
    private RequestManager requestManager;
    private final HashSet<RequestManagerFragment> childRequestManagerFragments
        = new HashSet<RequestManagerFragment>();
    private com.common.widget.glide.manager.RequestManagerFragment rootRequestManagerFragment;

    public RequestManagerFragment() {
        this(new com.common.widget.glide.manager.ActivityFragmentLifecycle());
    }

    // For testing only.
    @SuppressLint("ValidFragment")
    RequestManagerFragment(com.common.widget.glide.manager.ActivityFragmentLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    /**
     * Sets the current {@link RequestManager}.
     *
     * @param requestManager The request manager to use.
     */
    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    ActivityFragmentLifecycle getLifecycle() {
        return lifecycle;
    }

    /**
     * Returns the current {@link RequestManager} or null if none exists.
     */
    public RequestManager getRequestManager() {
        return requestManager;
    }

    public RequestManagerTreeNode getRequestManagerTreeNode() {
        return requestManagerTreeNode;
    }

    private void addChildRequestManagerFragment(com.common.widget.glide.manager.RequestManagerFragment child) {
        childRequestManagerFragments.add(child);
    }

    private void removeChildRequestManagerFragment(com.common.widget.glide.manager.RequestManagerFragment child) {
        childRequestManagerFragments.remove(child);
    }

    /**
     * Returns the set of fragments that this RequestManagerFragment's parent is a parent to. (i.e. our parent is
     * the fragment that we are annotating).
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Set<RequestManagerFragment> getDescendantRequestManagerFragments() {
        if (rootRequestManagerFragment == this) {
            return Collections.unmodifiableSet(childRequestManagerFragments);
        } else if (rootRequestManagerFragment == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Pre JB MR1 doesn't allow us to get the parent fragment so we can't introspect hierarchy, so just
            // return an empty set.
            return Collections.emptySet();
        } else {
            HashSet<RequestManagerFragment> descendants = new HashSet<RequestManagerFragment>();
            for (com.common.widget.glide.manager.RequestManagerFragment fragment
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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
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
                    .getRequestManagerFragment(getActivity().getFragmentManager());
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
    public void onTrimMemory(int level) {
        // If an activity is re-created, onTrimMemory may be called before a manager is ever set.
        // See #329.
        if (requestManager != null) {
            requestManager.onTrimMemory(level);
        }
    }

    @Override
    public void onLowMemory() {
        // If an activity is re-created, onLowMemory may be called before a manager is ever set.
        // See #329.
        if (requestManager != null) {
            requestManager.onLowMemory();
        }
    }

    private class FragmentRequestManagerTreeNode implements RequestManagerTreeNode {
        @Override
        public Set<RequestManager> getDescendants() {
            Set<RequestManagerFragment> descendantFragments = getDescendantRequestManagerFragments();
            HashSet<RequestManager> descendants =
                new HashSet<RequestManager>(descendantFragments.size());
            for (com.common.widget.glide.manager.RequestManagerFragment fragment : descendantFragments) {
                if (fragment.getRequestManager() != null) {
                    descendants.add(fragment.getRequestManager());
                }
            }
            return descendants;
        }
    }
}
