package com.zhuandian.znhl.control;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhuandian.znhl.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by 谢栋 on 2016/6/25.
 */
public class CurtainControl extends Activity{

    private TextView curtainTextView;
    private SeekBar  curtainSeekBar;
    private DatagramSocket ds=null;
    private String TAG="xiedong";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curtain);

        ((TextView) findViewById(R.id.toolbar_title)).setText("窗帘控制");  //设置toolbar标题

        //设置左上角按钮并绑定监听事件
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置左上角按钮并绑定点击事件
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        curtainSeekBar= (SeekBar) findViewById(R.id.curtainseekbar);
        curtainTextView= (TextView) findViewById(R.id.curtain);


        curtainSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                curtainTextView.setText(progress+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int str= seekBar.getProgress()*10;
                int str1=str+3;
                Log.v("dddd-->", "TYPE:3,CP:" + str + ",Sum:" + str1);
                sendInfo("TYPE:3,CP:"+str+",Sum:"+str1);
            }
        });
    }



    private void sendInfo(final String info) {

        Log.v(TAG,"info来啦"+info);
        new Thread() {

            @Override
            public void run() {


                try {
                    ds = new DatagramSocket(8086);


                    //建立字符输入流
                    BufferedReader bufr = new BufferedReader(new InputStreamReader(System.in));
                    String line = null;


                    Thread.sleep(500);
//                            if (flag == 1) {
//
//                                flag = 2;
                    byte buf[] = info.getBytes();

                    //把得到的数据封装进数据包中
                    DatagramPacket dp =
                            new DatagramPacket(buf, buf.length, InetAddress.getByName("192.168.1.2"), 8086);
                    //通过socket发送出去
                    Log.v("aaaa", "进来啦");
                    ds.send(dp);

                    //从服务端接收先关闭掉，socket为阻塞式操作，给服务端发送了数据，但是一直得不到服务端回馈的数据，所以该方法一直
                    //处于阻塞状态，所以利用语音发送的数据一直得不到响应

//                        Log.v("aaaa", "发出去啦");
//
//
//                        byte buf2[] = new byte[1024];
//
//                        //建立接受端的数据包，并且指定接收端的数据包
//                        DatagramPacket dp1 = new DatagramPacket(buf2, buf2.length);
//
//                        //利用socket的receive方法，接受数据
//                        ds.receive(dp1);
//
//                        str = new String(dp1.getData(), 0, dp1.getLength());
//                        Log.v("info2", str);


//                        //利用Handler把消息发送到主线程
//                        Message msg = new Message();
//                        msg.what = GET_INFO_TYPE0;
//                        msg.obj = str;
//                        handler.sendMessage(msg);
                    ds.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    @Override
    public void finish() {

        super.finish();

    }
}
