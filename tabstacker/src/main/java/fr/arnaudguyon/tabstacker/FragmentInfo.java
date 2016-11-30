package fr.arnaudguyon.tabstacker;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by aguyon on 28.11.16.
 */

class FragmentInfo {

    private static final String BUNDLE_FRAGMENT_CLASS = "fragment_class";
    private static final String BUNDLE_FRAGMENT_ARGUMENTS = "fragment_arguments";
    private static final String BUNDLE_FRAGMENT_DATA = "fragment_data";
    private static final String BUNDLE_TYPE = "type";
    private static final String BUNDLE_ANIMATION = "animation";

    Fragment mFragment;
    AnimationSet mAnimationSet;
    TabStacker.Type mType;

    FragmentInfo(Fragment fragment, AnimationSet animationSet, TabStacker.Type type) {
        mFragment = fragment;
        mAnimationSet = animationSet;
        mType = type;
    }

    static FragmentInfo restoreInstance(Bundle bundle) {

        String className = bundle.getString(BUNDLE_FRAGMENT_CLASS);
        Fragment fragment;
        try {
            // Class
            Class<Fragment> fragmentClass = (Class<Fragment>) Class.forName(className);
            fragment = fragmentClass.newInstance();

            // Arguments
            Bundle arguments = bundle.getBundle(BUNDLE_FRAGMENT_ARGUMENTS);
            fragment.setArguments(arguments);

            // Dynamic Data
            Bundle fragmentData = bundle.getBundle(BUNDLE_FRAGMENT_DATA);
            if ((fragmentData != null) && (fragment instanceof TabStacker.TabStackInterface)) {
                ((TabStacker.TabStackInterface) fragment).onRestoreTabFragmentInstance(fragmentData);
            }

            // Type of push
            TabStacker.Type type = TabStacker.Type.valueOf(bundle.getString(BUNDLE_TYPE));

            // Animations
            Bundle animation = bundle.getBundle(BUNDLE_ANIMATION);
            AnimationSet animationSet = AnimationSet.restoreInstance(animation);

            return new FragmentInfo(fragment, animationSet, type);

        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
        return null;
    }

    Bundle saveInstance() {
        Bundle bundle = new Bundle();

        // Class
        String fragmentClassName = mFragment.getClass().getName();
        bundle.putString(BUNDLE_FRAGMENT_CLASS, fragmentClassName);

        // Arguments
        Bundle fragmentArguments = mFragment.getArguments();
        bundle.putBundle(BUNDLE_FRAGMENT_ARGUMENTS, fragmentArguments);

        // Dynamic data
        if (mFragment instanceof TabStacker.TabStackInterface) {
            Bundle fragmentData = new Bundle();
            ((TabStacker.TabStackInterface) mFragment).onSaveTabFragmentInstance(fragmentData);
            bundle.putBundle(BUNDLE_FRAGMENT_DATA, fragmentData);
        }

        // Type of push
        String type = mType.name();
        bundle.putString(BUNDLE_TYPE, type);

        // Animations
        Bundle animation = (mAnimationSet != null) ? mAnimationSet.saveInstance() : null;
        bundle.putBundle(BUNDLE_ANIMATION, animation);

        return bundle;
    }

}