package com.example.cfy.polandtest;


import java.util.HashMap;
import Mathtype.Complex;
import Compiler.CodeCompiler;
import THREE.OpenGLRenderer;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	private ScrollView scroll=null;
	private EditText edittext=null;
	private Button button1=null;
	private int times=1;
	private ActionBar title;
	private String input;
	public static String name="MathNoteBook";
	public CodeCompiler compiler;
	private LinearLayout layout_content;
	private TextView text_output;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        compiler=new CodeCompiler(this);
		edittext=(EditText) findViewById(R.id.editText1);
		button1=(Button) findViewById(R.id.button1);
		scroll=(ScrollView) findViewById(R.id.scroll_content);
		layout_content=(LinearLayout) findViewById(R.id.layout_content);
		title=getActionBar();
        title.setBackgroundDrawable(new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawColor(Color.parseColor("#8fb5e8"));
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        });
		button1.setOnClickListener(this);
	}
	public Handler handler =new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==1){//sendprogress
					if(msg.obj instanceof String) title.setTitle(name+"--Running "+msg.obj.toString());
			}
			if(msg.what==2){//finished
				Object t3=msg.obj;
				TextView text1=text_output;
				text_output.setText("");
				//layout_content.addView(text1);
				text1.append(Html.fromHtml("<font color=\"#0000ff\" size=\"30\">Out"+"["+times+"]"+"=</font>"));
				if(t3 instanceof String){
					t3=t3.toString();
					text1.append(t3+"\n");
					scroll.fullScroll(ScrollView.FOCUS_DOWN);
					times++;
				}
				else if(t3 instanceof Bitmap){
					Matrix m=new Matrix();
					m.postScale((float)2,(float) 2);
					Bitmap t4=Bitmap.createBitmap((Bitmap) t3,0,0, ((Bitmap) t3).getWidth(), ((Bitmap) t3).getHeight(), m, true);
					ImageSpan is=new ImageSpan(MainActivity.this,(Bitmap) t4);
					SpannableString sp=new SpannableString("ou");
					sp.setSpan(is, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					text1.append("\n      ");
					text1.append(sp);
					text1.append("\n");
					scroll.fullScroll(ScrollView.FOCUS_DOWN);
					times++;
				}
                else if(t3 instanceof OpenGLRenderer){
                    MyGLView view=new MyGLView(MainActivity.this,(OpenGLRenderer)t3);
                    //layout_content.addView(view);
                    setContentView(view);
                }
                else if(t3 instanceof SpannableString){
                    text1.append((SpannableString)t3);
                    text1.setMovementMethod(LinkMovementMethod.getInstance());
                    text1.append("\n");
                    times++;
                }
				button1.setEnabled(true);
				title.setTitle(name);
			}
		} 
		
	};
	@Override
	public void onClick(View v) {
		HashMap<String,Complex> f=new HashMap<String,Complex>();
		f.put("PI",new Complex(Math.PI,0));
		f.put("E",new Complex(Math.E,0));

		input=edittext.getText().toString();
		if(!input.equals("clear")){
			TextView text_input=new TextView(MainActivity.this);
			text_output=new TextView(MainActivity.this);
			layout_content.addView(text_input);
			layout_content.addView(text_output);
			text_input.append(Html.fromHtml("<font color=\"#0000ff\" size=\"30\">In"+"["+times+"]"+":=</font>"));
			text_input.append(input);
			//text_output.append(Html.fromHtml("<font color=\"#0000ff\" size=\"30\">Out"+"["+times+"]"+":=</font>"));
			//text_input.append(Html.fromHtml("<font color=\"#0000ff\" size=\"30\">Out"+"["+times+"]"+":=</font>"));
			//Object t3=c.compile(edittext.getText().toString());
			Thread thread=new Thread(new myThread(input,this,text_output));
			thread.setDaemon(true);
			thread.start();
			button1.setEnabled(false);
		}
		else{
			layout_content.removeAllViews();
            CentralStation.map.clear();
		}
//		if(t3 instanceof String){
//			t3=t3.toString();
//			text1.append(Html.fromHtml("<font color=\"#0000ff\" size=\"30\">In"+"["+times+"]"+":=</font>"));
//			text1.append(edittext.getText().toString()+"\n");
//			text1.append(Html.fromHtml("<font color=\"#0000ff\" size=\"30\">Out"+"["+times+"]"+":=</font>"));
//			text1.append(t3+"\n\n");
//			scroll.fullScroll(ScrollView.FOCUS_DOWN);
//			times++;
//		}
//		else if(t3 instanceof Bitmap){
//			ImageSpan is=new ImageSpan(this,(Bitmap) t3);
//			SpannableString sp=new SpannableString("output");
//			sp.setSpan(is, 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//			text1.append(Html.fromHtml("<font color=\"#0000ff\" size=\"30\">In"+"["+times+"]"+":=</font>"));
//			text1.append(edittext.getText().toString()+"\n");
//			text1.append(Html.fromHtml("<font color=\"#0000ff\" size=\"30\">Out"+"["+times+"]"+":=</font>"));
//			text1.append(sp);
//			text1.append("\n\n");
//			scroll.fullScroll(ScrollView.FOCUS_DOWN);
//			times++;
//		}
	}
	public void changetext(TextView t,String s){
		t.setText(s);
	}
	private class myThread implements Runnable{
		private String s;
		private Activity a;
		private TextView progresstext;
		public myThread(String s,Activity a,TextView progresstext){
			this.s=s;
			this.a=a;
			this.progresstext=progresstext;
		}
		@Override
		public void run() {
			Object b=compiler.compile(s,progresstext);
			Message msg=new Message();
			msg.what=2;
			msg.obj=b;
			((MainActivity) a).handler.sendMessage(msg);
		}
	}
}
