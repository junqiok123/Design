package com.example.design.fragment;

import android.support.v4.app.Fragment;

public abstract class LazyFragment extends Fragment {

	public boolean isVisibleToUser;

	public boolean isViewCreated = false;

	/**
	 * 懒加载
	 */
	public abstract void lazyLoad();

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		this.isVisibleToUser = isVisibleToUser;
		if (isVisibleToUser) {
			lazyLoad();
		}
	}
}
