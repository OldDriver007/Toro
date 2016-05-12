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

package im.ene.lab.toro.sample.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import com.devbrackets.android.exomedia.EMVideoView;
import im.ene.lab.toro.Toro;
import im.ene.lab.toro.sample.R;

/**
 * Created by eneim on 5/12/16.
 */
public class PlayerDialogFragment extends DialogFragment
    implements MediaPlayer.OnCompletionListener {

  public static final String TAG = "PlayerDialogFragment";

  @Override public void onCompletion(MediaPlayer mp) {
    dismissAllowingStateLoss();
  }

  public static class Param {
    public final String video;
    public final int timeStamp;

    public Param(String video, int timeStamp) {
      this.video = video;
      this.timeStamp = timeStamp;
    }
  }

  public static final String ARGS_LATEST_TIMESTAMP = "player_latest_timestamp";

  private static final String PARAM_VIDEO = "player_dialog_param_video";
  private static final String PARAM_START_POINT = "player_dialog_param_start_point";

  public static PlayerDialogFragment newInstance(Param param) {
    Bundle args = new Bundle();
    args.putString(PARAM_VIDEO, param.video);
    args.putInt(PARAM_START_POINT, param.timeStamp);
    PlayerDialogFragment fragment = new PlayerDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  private Param mParam;

  @Bind(R.id.video_view) EMVideoView mVideoView;
  @BindDimen(R.dimen.player_dialog_height) int dialogHeight;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    mParam = new Param(args.getString(PARAM_VIDEO), args.getInt(PARAM_START_POINT));
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_dialog_player, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Dialog dialog = getDialog();
    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
    lp.height = dialogHeight;
    dialog.getWindow().setAttributes(lp);

    mVideoView.setVideoPath(mParam.video);
    mVideoView.setOnCompletionListener(this);
    mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override public void onPrepared(MediaPlayer mp) {
        mVideoView.seekTo(mParam.timeStamp);
        mVideoView.start();
      }
    });
  }

  @Override public void onPause() {
    super.onPause();
    mVideoView.pause();
  }

  @Override public void onResume() {
    super.onResume();
    mVideoView.start();
  }

  @Override public void onDestroyView() {
    mVideoView.release();
    super.onDestroyView();
  }

  @Override public void show(FragmentManager manager, String tag) {
    Toro.rest(true);
    super.show(manager, tag);
  }

  @Override public void onDismiss(DialogInterface dialog) {
    if (getTargetFragment() != null) {
      int latestPosition = (int) mVideoView.getCurrentPosition();
      Intent result = new Intent();
      result.putExtra(ARGS_LATEST_TIMESTAMP, latestPosition);
      getTargetFragment().onActivityResult(1000, Activity.RESULT_OK, result);
    }
    Toro.rest(false);
    super.onDismiss(dialog);
  }
}
