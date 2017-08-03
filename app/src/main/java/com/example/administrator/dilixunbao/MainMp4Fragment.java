package com.example.administrator.dilixunbao;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.dilixunbao.commons.ActivityUtils;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by Administrator on 2017/7/31.
 */

public class MainMp4Fragment extends Fragment implements TextureView.SurfaceTextureListener {
    private TextureView mTextureView;
    private MediaPlayer mMediaPlayer;
    private ActivityUtils mActivityUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         /*
        * MediaPlayer播放视频
        * TextureView用于展示播放的视频
        *
        * 何时播放视频
        * TextureView
        * */
        mTextureView = new TextureView(getContext());
        return mTextureView;
    }

    //在onCreateView执行完毕之后执行
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivityUtils = new ActivityUtils(this);

        /*
        * TextureView何时准备好
        * 添加监听
        * */
        mTextureView.setSurfaceTextureListener(this);
    }

    //-----------------------------------------TextureView监听实现的方法----------------------------------
    //当TextureView准备好的时候
    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, int i, int i1) {
  /*
        * TextureView准备好了
        * 1、获取视频资源
        * 2、初始化视频播放控件，即MediaPlayer
        * 3、将视频资源设置给视频播放控件
        * 4、将视频添加到TextureView进行播放
        * */
        try {
            //1、获取视频资源
            AssetFileDescriptor assetFileDescriptor = getContext().getAssets().openFd("welcome.mp4");
            //获取想要的文件类型
            FileDescriptor fileDescriptor = assetFileDescriptor.getFileDescriptor();
            //2、初始化视频播放控件，即MediaPlayer
            mMediaPlayer = new MediaPlayer();
            //3、将视频资源设置给视频播放控件
            mMediaPlayer.setDataSource(fileDescriptor, assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            //异步准备
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Surface mSurface = new Surface(surfaceTexture);
                    //4、将视频添加到TextureView进行播放
                    mMediaPlayer.setSurface(mSurface);
                    mMediaPlayer.setLooping(true); //设置循环播放
                    mMediaPlayer.start();//开始播放
                }
            });
        } catch (IOException e) {
            mActivityUtils.showToast("视频播放失败了：" + e.getMessage());
        }
    }

    //当TextureView尺寸发生改变的时候执行
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    //当TextureView销毁的时候
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    //更新的时候
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer = null;
        }
    }
}
