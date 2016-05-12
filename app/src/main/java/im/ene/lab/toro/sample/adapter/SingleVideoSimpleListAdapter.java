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

package im.ene.lab.toro.sample.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import im.ene.lab.toro.ToroPlayer;
import im.ene.lab.toro.VideoPlayerManager;
import im.ene.lab.toro.VideoPlayerManagerImpl;
import im.ene.lab.toro.sample.data.SimpleObject;

/**
 * Created by eneim on 2/3/16.
 */
public class SingleVideoSimpleListAdapter extends BaseSampleAdapter implements VideoPlayerManager {

  private final View.OnClickListener listener;

  private VideoPlayerManagerImpl mDelegate;

  public SingleVideoSimpleListAdapter(View.OnClickListener listener) {
    this.listener = listener;
    mDelegate = new VideoPlayerManagerImpl();
  }

  @Nullable @Override protected Object getItem(int position) {
    if (position == 1) {
      return mVideos.get(0);
    }
    return new SimpleObject();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
    viewHolder.setOnItemClickListener(listener);

    return viewHolder;
  }

  @Override public int firstVideoPosition() {
    return 1;
  }

  @Override public ToroPlayer getPlayer() {
    return mDelegate.getPlayer();
  }

  @Override public void setPlayer(ToroPlayer player) {
    mDelegate.setPlayer(player);
  }

  @Override public void onRegistered() {
    mDelegate.onRegistered();
  }

  @Override public void onUnregistered() {
    mDelegate.onUnregistered();
  }

  @Override public void startPlayback() {
    mDelegate.startPlayback();
  }

  @Override public void pausePlayback() {
    mDelegate.pausePlayback();
  }

  @Override public void saveVideoState(String videoId, @Nullable Integer position, long duration) {
    mDelegate.saveVideoState(videoId, position, duration);
  }

  @Override public void restoreVideoState(String videoId) {
    mDelegate.restoreVideoState(videoId);
  }
}
