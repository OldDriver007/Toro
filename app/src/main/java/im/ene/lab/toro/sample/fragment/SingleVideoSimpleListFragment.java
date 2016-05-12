/*
 * Copyright 2016 eneim@Eneim Labs, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.ene.lab.toro.sample.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import im.ene.lab.toro.ToroAdapter;
import im.ene.lab.toro.ToroVideoViewHolder;
import im.ene.lab.toro.VideoPlayerManager;
import im.ene.lab.toro.sample.adapter.SingleVideoSimpleListAdapter;
import im.ene.lab.toro.sample.data.VideoSource;
import im.ene.lab.toro.sample.widget.PlayerDialogFragment;

/**
 * Created by eneim on 2/3/16.
 */
public class SingleVideoSimpleListFragment extends RecyclerViewFragment
    implements View.OnClickListener {

  public static final String TAG = "SingleVideoSimpleListFragment";

  public static SingleVideoSimpleListFragment newInstance() {
    return new SingleVideoSimpleListFragment();
  }

  @NonNull @Override protected RecyclerView.LayoutManager getLayoutManager() {
    return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
  }

  @NonNull @Override protected RecyclerView.Adapter getAdapter() {
    return new SingleVideoSimpleListAdapter(this);
  }

  @Override public void onClick(View v) {
    ToroAdapter.ViewHolder viewHolder =
        (ToroAdapter.ViewHolder) mRecyclerView.findContainingViewHolder(v);
    if (viewHolder instanceof ToroVideoViewHolder) {
      PlayerDialogFragment player = PlayerDialogFragment.newInstance(
          new PlayerDialogFragment.Param(VideoSource.SOURCES[0],
              ((ToroVideoViewHolder) viewHolder).getCurrentPosition()));
      player.setTargetFragment(this, 1000);
      player.show(getChildFragmentManager(), PlayerDialogFragment.TAG);
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1000) {
      VideoPlayerManager manager = ((VideoPlayerManager) mAdapter);
      int latestPosition = data.getIntExtra(PlayerDialogFragment.ARGS_LATEST_TIMESTAMP, 0);
      manager.saveVideoState(manager.getPlayer().getVideoId(), latestPosition,
          manager.getPlayer().getDuration());
    }
  }

  @Override public void onResume() {
    super.onResume();
  }
}
