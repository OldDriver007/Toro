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

package im.ene.lab.toro.sample.facebook;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import im.ene.lab.toro.ToroAdapter;
import im.ene.lab.toro.ToroPlayer;
import im.ene.lab.toro.VideoPlayerManager;
import im.ene.lab.toro.VideoPlayerManagerImpl;
import im.ene.lab.toro.sample.adapter.OrderedVideoList;
import im.ene.lab.toro.sample.data.SimpleObject;
import im.ene.lab.toro.sample.data.SimpleVideoObject;
import im.ene.lab.toro.sample.data.VideoSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eneim on 5/13/16.
 */
public class FbFeedAdapter extends ToroAdapter<ToroAdapter.ViewHolder>
    implements VideoPlayerManager, OrderedVideoList {

  private final VideoPlayerManager delegate;
  private OnItemClickListener clickListener;

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.clickListener = onItemClickListener;
  }

  protected List<SimpleVideoObject> mVideos = new ArrayList<>();

  public FbFeedAdapter() {
    this.delegate = new VideoPlayerManagerImpl();
    for (String item : VideoSource.SOURCES) {
      this.mVideos.add(new SimpleVideoObject(item));
    }
  }

  @Nullable @Override protected Object getItem(int position) {
    return position % 3 == 1 ? mVideos.get((position - 1) % mVideos.size()) : new SimpleObject();
  }

  @Override public int getItemViewType(int position) {
    return getItem(position) instanceof SimpleVideoObject ? FbItemViewHolder.POST_TYPE_VIDEO
        : (position % 5 == 2 ? FbItemViewHolder.POST_TYPE_TEXT
            : FbItemViewHolder.POST_TYPE_PHOTO);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, @FbItemViewHolder.PostType int viewType) {
    final ViewHolder viewHolder = FbItemViewHolder.createViewHolder(parent, viewType);
    if (viewHolder instanceof FbItemViewHolder.VideoPost) {
      viewHolder.setOnItemClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          int pos = viewHolder.getAdapterPosition();
          if (pos == RecyclerView.NO_POSITION || clickListener == null) {
            return;
          }

          clickListener.onItemClick(FbFeedAdapter.this, viewHolder, v, pos, getItemId(pos));
        }
      });
    }

    return viewHolder;
  }

  @Override public int getItemCount() {
    return 512; // magic number
  }

  @Override public ToroPlayer getPlayer() {
    return delegate.getPlayer();
  }

  @Override public void setPlayer(ToroPlayer player) {
    delegate.setPlayer(player);
  }

  @Override public void onRegistered() {
    delegate.onRegistered();
  }

  @Override public void onUnregistered() {
    delegate.onUnregistered();
  }

  @Override public void startPlayback() {
    delegate.startPlayback();
  }

  @Override public void pausePlayback() {
    delegate.pausePlayback();
  }

  @Override public void saveVideoState(String videoId, @Nullable Integer position, long duration) {
    delegate.saveVideoState(videoId, position, duration);
  }

  @Override public void restoreVideoState(String videoId) {
    delegate.restoreVideoState(videoId);
  }

  @Nullable @Override public Integer getSavedPosition(String videoId) {
    return delegate.getSavedPosition(videoId);
  }

  @Override public int firstVideoPosition() {
    return 1;
  }
}
