package andloo.myapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    TextView play;

    //屏幕清除的标志，用以在每次运算后为true
    Boolean flag_clean = false;
    //运算符的标志
    Boolean flag_operation = false;
    //小数点的标志
    Boolean flag_point = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = (TextView)findViewById(R.id.textView);
    }

    public void onClick(View v) {
        String st = play.getText().toString();
        switch (v.getId()){
            case R.id.textView:
                if(!st.equals("")){

                    //首先，获取剪贴板服务
                    ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    //然后把数据放在ClipData对象中
                    ClipData clip = ClipData.newPlainText("play",st);
                    //clipdata添加到clipboard
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(this,"已复制到剪贴板",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"没有可复制的内容",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                if(flag_clean){
                    play.setText("");
                    st = "";
                    flag_clean=false;
                }
                if (flag_operation){
                    flag_operation = play.getText().toString().contains(" ");
                }
                play.setText(st+((Button)v).getText());
                break;
            case R.id.btn_point:

                if(!st.equals("")) {
                    //不为空
                    if (flag_clean) {
                        //需要清屏
                        play.setText("");
                        st = "";
                        flag_clean = false;
                        play.setText(st+"0" + ((Button) v).getText());
                        flag_point = true;
                    } else if (flag_operation) {  //不需清屏
                        //含有运算符
                        if (st.lastIndexOf(" ") == st.length()-1) {
                            //以运算符结尾
                            play.setText(st + "0" + ((Button) v).getText());
                            flag_point = true;
                        } else if (st.lastIndexOf(".") > st.lastIndexOf(" ")) {
                            //小数点在运算符后
                            break;
                        }else {
                            play.setText(st + ((Button) v).getText());
                        }
                    } else if (!st.contains(".")) {
                        //没有运算符,不含小数点
                        play.setText(st + ((Button) v).getText());
                        flag_point = true;
                    } else {
                        //没有运算符，含小数点
                        break;
                    }
                }else{
                    //为空
                    play.setText(st + "0" + ((Button) v).getText());
                    flag_point = true;
                }
                break;
            case R.id.btn_jia:
            case R.id.btn_jian:
            case R.id.btn_cheng:
            case R.id.btn_chu:
                if(!st.equals("")) {
                    if (flag_clean) {
                        play.setText("");
                        st = "";
                    } else if (flag_operation) {
                        break;
                    }
                    play.setText(st + " " + ((Button) v).getText() + " ");
                    flag_operation = true;
                    flag_point = false;
                }
                break;
            case R.id.btn_backspace:
                //判断是否为空
                if(!st.equals("")){
                    //判断是否以运算符结尾
                    if (st.length()>2){
                        if (st.substring(st.length()-2,st.length()).equals("+ ")||st.substring(st.length()-2,st.length()).equals("- ")||st.substring(st.length()-2,st.length()).equals("× ")||st.substring(st.length()-2,st.length()).equals("÷ ")){
                            //如果有运算符，截取运算符且设置运算符flag为false
                            play.setText(st.substring(0,st.length()-3));
                            flag_operation = false;
                        }else {
                            play.setText(st.substring(0, st.length()-1));
                        }

                    }else{
                        play.setText(st.substring(0,st.length()-1));
                    }

                }
                break;
            case R.id.btn_ac:
                play.setText(R.string.txv_play);
                flag_clean = false;
                flag_operation = false;
                flag_point = false;
                break;
            case R.id.btn_deng:
                getResult();
                flag_operation = false;
                flag_point = false;
                break;
    }

    }
    //运算方法
    private void getResult(){
        //获取屏幕内容
        String question = play.getText().toString();
        //判断是否为空
        if(question.equals("")){
            return;
        }
        //是否有运算符
        if(!question.contains(" ")){
            return;
        }
        //获取前一个数
        String s1 = question.substring(0 , question.indexOf(" "));
        //运算符
        String op = question.substring(question.indexOf(" ")+1 , question.indexOf(" ")+2);
        //后一个数
        String s2 = question.substring(question.indexOf(" ")+3);
        //两数不为空的运算

        Boolean flag_chu0 = false;
        if(!s1.equals("")&&!s2.equals("")){
            double dr = 0;
            double d1 = Double.parseDouble(s1);
            double d2 = Double.parseDouble(s2);
            if(op.equals("+")){
                dr = d1+d2;
            }else if(op.equals("-")){
                dr = d1-d2;
            }else if(op.equals("×")){
                dr = d1*d2;
            }else if(op.equals("÷")){
                if(d2==0){
                    flag_chu0=true;
                }else{
                    dr = d1/d2;
                }
            }

            if (flag_chu0){
                play.setText("0不能作分母");
            }else if(dr%1==0){
                //判断结果整数
                int r = (int)dr;
                play.setText(r+"");
            }else{
                play.setText(dr+"");
            }

        }else if(!s1.equals("")&&s2.equals("")){
            play.setText(question);
        }else if (s1.equals("")&&!s2.equals("")){
            //前数为空
            double dr = 0;
            double d2 = Double.parseDouble(s2);
            if(op.equals("+")){
                dr = 0+d2;
            }else if(op.equals("-")){
                dr = 0-d2;
            }else if(op.equals("×")){
                dr = 0*d2;
            }else if(op.equals("÷")){
                if(d2==0){
                    flag_chu0=true;
                }else{
                    dr = 0;
                }
            }

            if (flag_chu0){
                play.setText("0不能作分母");
            }else if(dr%1==0){
                //判断结果整数
                int r = (int)dr;
                play.setText(r+"");
            }else{
                play.setText(dr+"");
            }

        }

        flag_clean = true;
    }
}
