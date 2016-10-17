package andloo.myapp;

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
    Boolean flag_clean = false;
    Boolean flag_operation = false;
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
                Toast.makeText(this,"已复制到剪贴板",Toast.LENGTH_LONG).show();
                //首先，获取剪贴板服务
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
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
                    flag_operation = play.getText().toString().contains("+")||play.getText().toString().contains("-")||play.getText().toString().contains("×")||play.getText().toString().contains("÷");
                }
                play.setText(st+((Button)v).getText());
                break;
            case R.id.btn_point:
                if(flag_clean){
                    play.setText("");
                    st = "";
                    flag_clean=false;
                    play.setText(st+"0"+((Button)v).getText());
                }
                if (flag_operation){
                   // flag_operation = play.getText().toString().contains("+")||play.getText().toString().contains("-")||play.getText().toString().contains("×")||play.getText().toString().contains("÷");
                    if (flag_point){
                        break;
                    }else {
                        play.setText(st+((Button)v).getText());
                        Log.d("MainActivity","flag_point else");
                        flag_point = true;
                        break;
                    }

                }
                if(st!=null&&!st.equals("")){
                    if (flag_point){
                        break;
                    }else {
                        play.setText(st+((Button)v).getText());
                        Log.d("MainActivity","null else");
                        flag_point = true;
                        break;
                    }
                }else{
                    play.setText(st+"0"+((Button)v).getText());
                }
            case R.id.btn_jia:
            case R.id.btn_jian:
            case R.id.btn_cheng:
            case R.id.btn_chu:
                if (flag_clean){
                    play.setText("");
                    st = "";
                }
                if (flag_operation){
                    break;
                }
                play.setText(st+" "+((Button)v).getText()+" ");
                flag_operation = true;
                flag_point = false;
                break;
            case R.id.btn_backspace:
                if(st!=null&&!st.equals("")){
                    play.setText(st.substring(0,st.length()-1));
                    flag_operation = play.getText().toString().contains("+")||play.getText().toString().contains("-")||play.getText().toString().contains("×")||play.getText().toString().contains("÷");

                    break;
                }else {
                    break;
                }
            case R.id.btn_ac:
                play.setText(R.string.txv_play);
                flag_clean = false;
                flag_operation = false;
                flag_point = false;
            case R.id.btn_deng:
                getResult();
                flag_operation = false;
                break;
    }

    }
    //运算方法
    private void getResult(){
        //获取屏幕内容
        String question = play.getText().toString();
        //判断是否为空
        if(question==null||question.equals("")){
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
