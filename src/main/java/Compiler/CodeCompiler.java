package Compiler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.cfy.polandtest.Activity_view3dResult;
import com.example.cfy.polandtest.CentralStation;
import com.example.cfy.polandtest.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import Mathtype.Complex;
import Mathtype.FormulaCompiler;
import Mathtype.Thouway_away;
import THREE.EditableMesh;
import THREE.EditableSurface;
import THREE.OpenGLRenderer;
import THREE.VerticesLengthNotMatchException;

public class CodeCompiler implements Serializable{
    private HashMap<String, Object> objects=null;
    private FormulaCompiler compiler=null;
    private Activity a;
    private TextView progresstext;
    private String temp;
    public CodeCompiler(Activity a){
        objects=new HashMap<String, Object>();
        compiler=new FormulaCompiler();
        compiler.setVarible("E",new Complex(Math.E, 0));
        compiler.setVarible("PI",new Complex(Math.PI, 0));
        this.a=a;
    }
    public Object compile(String s,TextView progresstext){
        this.progresstext=progresstext;
        if(s.equals(s.replace("[","1"))&&s.equals(s.replace("=","1"))) s="N["+s+"]";
        return analyse(s,null);
    }
    private Object analyse(String s,String[] args){

        Matcher m_setvalue=Pattern.compile("^([a-zA-Z0-9]*)=(.*)$").matcher(s);
        Matcher m_number=Pattern.compile("^N\\[(.*)\\]$").matcher(s);
        Matcher m_repeat=Pattern.compile("^repeat\\[(.*),\\{([a-zA-Z0-9]*),(.*)\\}\\]$").matcher(s);
        Matcher m_cplot=Pattern.compile("^cplot\\[(.*),\\{([a-zA-Z0-9]*),(.*),(.*),(.*),(.*)\\}\\]$").matcher(s);
        Matcher m_cplot2=Pattern.compile("^cplot\\[(.*),\\{([a-zA-Z0-9]*),(.*),(.*),(.*),(.*)\\},(.*)\\]$").matcher(s);
        Matcher m_plot=Pattern.compile("^plot\\[(.*),(.*),\\{([a-zA-Z0-9]*),(.*),(.*)\\}\\]$").matcher(s);
        Matcher m_plot2=Pattern.compile("^plot\\[(.*),\\{([a-zA-Z0-9]*),(.*),(.*),(.*),(.*)\\},(.*)\\]$").matcher(s);
        Matcher m_frame=Pattern.compile("^frame\\[(.*\\]),(.*\\])\\]$").matcher(s);
        Matcher m_plot3d=Pattern.compile("^plot3d\\[(.+),(.+),(.+),\\{([a-zA-Z0-9]*),(.*),(.*)\\},\\{([a-zA-Z0-9]*),(.*),(.*)\\}\\]$").matcher(s);
        Matcher m_plot3dline=Pattern.compile("^plot3d\\[(.+),(.+),(.+),\\{([a-zA-Z0-9]*),(.*),(.*)\\}\\]$").matcher(s);
        Matcher m_play=Pattern.compile("^play\\[(.+),\\{\\}\\]$").matcher(s);
        try {
            if (m_number.matches()) {
                if (s.equals("N[啊，是吧]")) {
                    return "锁尔";
                } else if (s.equals("N[锁尔]")) {
                    return "禁用锁尔";
                } else {
                    compiler.compile(analyse(m_number.group(1),null).toString());
                    return compiler.calculate().toString();
                }
            }
            else if (m_cplot.matches()) {
                String formula = analyse(m_cplot.group(1),null).toString();
                String varible = m_cplot.group(2);
                double reStart = Double.parseDouble(analyse("N["+m_cplot.group(3)+"]",null).toString());
                double imStart = Double.parseDouble(analyse("N["+m_cplot.group(4)+"]",null).toString());
                double reEnd = Double.parseDouble(analyse("N["+m_cplot.group(5)+"]",null).toString());
                double imEnd = Double.parseDouble(analyse("N["+m_cplot.group(6)+"]",null).toString());
                return cplot(formula, varible, reStart, imStart, reEnd, imEnd,1);
            }
            else if (m_repeat.matches()) {
                int times = (int)Double.parseDouble(analyse(m_repeat.group(3),null).toString());
                String s1 = m_repeat.group(1);
                String output = s1;
                String f = m_repeat.group(2);
                for (int i = 0; i < times; i++) {
                    output = output.replace(f, "(" + s1 + ")");
                }
                return output;
            }
            else if(m_cplot2.matches()){
                int times=(int)Double.parseDouble(analyse("N["+m_cplot2.group(7)+"]",null).toString());
                String formula = analyse(m_cplot2.group(1),null).toString();
                String varible = m_cplot2.group(2);
                double reStart = Double.parseDouble(analyse("N["+m_cplot2.group(3)+"]",null).toString());
                double imStart = Double.parseDouble(analyse("N["+m_cplot2.group(4)+"]",null).toString());
                double reEnd = Double.parseDouble(analyse("N["+m_cplot2.group(5)+"]",null).toString());
                double imEnd = Double.parseDouble(analyse("N["+m_cplot2.group(6)+"]",null).toString());
                return cplot(formula, varible, reStart, imStart, reEnd, imEnd,times);
            }
            else if(m_plot.matches()){
                String formula1 = analyse(m_plot.group(1),null).toString();
                String formula2 = analyse(m_plot.group(2),null).toString();
                String varible = m_plot.group(3);
                double pStart = Double.parseDouble(analyse("N["+m_plot.group(4)+"]",null).toString());
                double pEnd = Double.parseDouble(analyse("N["+m_plot.group(5)+"]",null).toString());
                if(args!=null&&args[0].equals("false")) return plot(formula1, formula2,varible,pStart,pEnd,false);
                return plot(formula1, formula2,varible,pStart,pEnd,true);
            }
            else if(m_setvalue.matches()){
                String name=m_setvalue.group(1);
                compiler.compile(m_setvalue.group(2));
                Complex value=compiler.calculate();
                compiler.setVarible(name, value);
                return value.toString();
            }
            else if(m_frame.matches()){
                Object b1=analyse(m_frame.group(1),new String[]{"false"});
                Object b2=analyse(m_frame.group(2),null);
                if(b1 instanceof Bitmap&&b2 instanceof Bitmap){
                    Bitmap temp=Bitmap.createBitmap((Bitmap) b2);
                    Canvas canvas=new Canvas(temp);
                    Paint paint=new Paint();
                    paint.setColor(0x00ff00);
                    paint.setAlpha(255);
                    canvas.drawBitmap((Bitmap) b1, 20, 20, paint);
                    return temp;
                }
                else{
                    return "invalid input";

                }
            }
            else if(m_plot3d.matches()){
                final String formula1=m_plot3d.group(1);
                String formula2=m_plot3d.group(2);
                String formula3=m_plot3d.group(3);
                String variable1=m_plot3d.group(4);
                String variable2=m_plot3d.group(7);
                double v1Start=Double.parseDouble(analyse("N["+m_plot3d.group(5)+"]",null).toString());
                double v1End=Double.parseDouble(analyse("N["+m_plot3d.group(6)+"]",null).toString());
                double v2Start=Double.parseDouble(analyse("N["+m_plot3d.group(8)+"]",null).toString());
                double v2End=Double.parseDouble(analyse("N["+m_plot3d.group(9)+"]",null).toString());
                OpenGLRenderer r=plot3d(formula1,formula2,formula3,variable1,variable2,v1Start,v1End,v2Start,v2End);
                temp=CentralStation.map.size()+"";
                CentralStation.map.put(temp,r);
                SpannableString ss=new SpannableString("Click to view result");

                ss.setSpan(new ClickableSpan() {
                    final String s=temp;
                    @Override
                    public void onClick(View widget) {
                        Intent intent=new Intent(a,Activity_view3dResult.class);

                        intent.putExtra("aaa",s);
                        a.startActivity(intent);
                    }
                },0,"Click to view result".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //return plot3d(formula1,formula2,formula3,variable1,variable2,v1Start,v1End,v2Start,v2End);
                return ss;
            }
            else if(m_plot3dline.matches()){
                String formula1=m_plot3dline.group(1);
                String formula2=m_plot3dline.group(2);
                String formula3=m_plot3dline.group(3);
                String variable=m_plot3dline.group(4);
                double v1Start=Double.parseDouble(analyse("N["+m_plot3dline.group(5)+"]",null).toString());
                double v1End=Double.parseDouble(analyse("N["+m_plot3dline.group(6)+"]",null).toString());
                OpenGLRenderer r= plot3dline(formula1,formula2,formula3,variable,v1Start,v1End);
                temp=CentralStation.map.size()+"";
                CentralStation.map.put(temp,r);
                SpannableString ss=new SpannableString("Click to view result");

                ss.setSpan(new ClickableSpan() {
                    final String s=temp;
                    @Override
                    public void onClick(View widget) {
                        Intent intent=new Intent(a,Activity_view3dResult.class);

                        intent.putExtra("aaa",s);
                        a.startActivity(intent);
                    }
                },0,"Click to view result".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //return plot3d(formula1,formula2,formula3,variable1,variable2,v1Start,v1End,v2Start,v2End);
                return ss;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //if(e.equals(new java.lang.NumberFormatException())) return "";
            return e.toString();
        }
        return s;
    }
    private Button playsound(){
        //TODO play sound out of a function
        int[] sound1={0x52,0x49,0x46,0x46,0x00,0x01,0x00,0x00,0x57,0x41,0x56,0x45,0x66,0x6d,0x74,0x20,0x10,0x00,0x00,0x00,0x01,0x00,0x01,0x00,0x00,0x10,0x00,0x00,0x00,0x10,0x00,0x00,0x01,0x00,0x10,0x00,0x64,0x61,0x74,0x61,0x20,0x00,0x00,0x00,0x10,0x20,0x30,0x40,0x50,0x60,0x70,0x80,0x80,0xc2,0x90,0xc2,0xa0,0xc2,0xb0,0xc3,0x80,0xc3,0x90,0xc3,0xa0,0xc3,0xb0,0x01,0x20,0x20,0x20,0x20,0x20,0x20,0x20,0x20,0x20,0x20,0x20,0x0a};
        byte[] sound=new byte[sound1.length];
        for(int i=0;i<sound1.length;i++){
            sound[i]=(byte)sound1[i];
        }
        return null;
    }
    private boolean iscomplex(String s){
        return Pattern.compile("[a-z0-9*\\(\\). ]*").matcher(s).matches();
    }
    private Bitmap cplot(String formula,String varible,double reStart,double imStart,double reEnd,double imEnd,int time) throws Thouway_away{
        Bitmap output=Bitmap.createBitmap(300, 300,Config.RGB_565);

        compiler.compile(formula);
        for(int i=0;i<output.getWidth();i++){
            for(int j=0;j<output.getHeight();j++){
                double x=reStart+i*(reEnd-reStart)/(output.getWidth()-1);
                double y=imEnd+j*(imStart-imEnd)/(output.getHeight()-1);
                //compiler.compile(formula);
                compiler.setVarible(varible, new Complex(x, y));
                Complex result=compiler.calculate();
                for(int t=0;t<time-1;t++){
                    compiler.setVarible(varible, result);
                    result=compiler.calculate();
                }
                output.setPixel(i, j, getColor(result));

                float p=(float) (Math.ceil(1000*i/output.getWidth())/10);
                sendprogress(p+"%");
            }
        }
        Canvas canvas=new Canvas(output);
        Paint paint=new Paint();
        paint.setColor(Color.parseColor("#0000ff"));
        paint.setAlpha(255);
        paint.setStyle(Style.STROKE);
        canvas.drawRect(0, 0, output.getWidth()-1, output.getHeight()-1,paint);
        return output;
    }
    private Bitmap plot(String formula1,String formula2,String variable,double pStart,double pEnd,boolean addlabel) throws Thouway_away{
        Bitmap output=Bitmap.createBitmap(300,300, Config.ARGB_8888);
        double xStart=0;
        double xEnd=0;
        double yStart=0;
        double yEnd=0;
        Canvas canvas=new Canvas();
        canvas.setBitmap(output);
        canvas.drawColor(Color.TRANSPARENT);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0x0000ff);
        paint.setAlpha(255);
        paint.setStyle(Style.STROKE);
        canvas.drawRect(0, 0, output.getWidth()-1, output.getHeight()-1, paint);
        Path path=new Path();
        paint.setColor(0xff0000);
        paint.setAlpha(255);
        path.moveTo(1, 1);
        FormulaCompiler compiler2=(FormulaCompiler)compiler.clone();
        compiler.compile(formula1);
        compiler2.compile(formula2);
        for(int i=0;i<300;i++){
            double p=pStart+(pEnd-pStart)/(300-1)*((float) i);
            compiler.setVarible(variable,new Complex(p,0));
            compiler2.setVarible(variable,new Complex(p,0));
            double x=compiler.calculate().Re();
            double y=compiler2.calculate().Re();
            if(i==0){
                path.moveTo((float)x,(float)y);
                xStart=x;
                xEnd=x;
                yStart=y;
                yEnd=y;
            }
            else{
                path.lineTo((float)x,(float)y);
                xStart=(xStart<=x)?xStart:x;
                xEnd=(xEnd>=x)?xEnd:x;
                yStart=(yStart<=y)?yStart:y;
                yEnd=(yEnd>=y)?yEnd:y;
                sendprogress(i/3+"%");
            }
        }
        Matrix m=new Matrix();
        m.postTranslate((float)-xStart,(float)-yEnd);
        m.postScale((float)(((float)output.getWidth())/(-xStart+xEnd)),(float)(((float)output.getHeight())/(-yEnd+yStart)));
        path.transform(m);
        canvas.drawPath(path, paint);
        if(addlabel) return addScaleLabel(output,(float)Math.ceil(xStart*1000)/1000,(float)Math.ceil(xEnd*1000)/1000,(float)Math.ceil(yStart*1000)/1000,(float)Math.ceil(yEnd*1000)/1000);
        return output;
    }
    private OpenGLRenderer plot3d(String formula1,String formula2,String formula3,String variable1,String variable2,double v1Start,double v1End,double v2Start,double v2End) throws Thouway_away, VerticesLengthNotMatchException{
        //TODO plot 3D surface
        double xMin=0,xMax=0,yMin=0,yMax=0,zMin=0,zMax=0;
        int hs=150;
        int ws=150;
        float[] vertices=new float[(hs+1)*(ws+1)*3];
        FormulaCompiler compiler2=(FormulaCompiler)compiler.clone();
        FormulaCompiler compiler3=(FormulaCompiler)compiler.clone();
        compiler.compile(formula1);
        compiler2.compile(formula2);
        compiler3.compile(formula3);
        OpenGLRenderer renderer=new OpenGLRenderer(null);
        EditableSurface s=new EditableSurface(20,20,ws,hs);
        int currentIndex=0;
        for(int i=0;i<=hs;i++){
            for(int j=0;j<=ws;j++){
                double l=v1Start+(v1End-v1Start)/hs*((double)j);
                double m=v2Start+(v2End-v2Start)/ws*(double)i;
                compiler.setVarible(variable1,new Complex(l,0));
                compiler.setVarible(variable2,new Complex(m,0));
                compiler2.setVarible(variable1,new Complex(l,0));
                compiler2.setVarible(variable2,new Complex(m,0));
                compiler3.setVarible(variable1,new Complex(l,0));
                compiler3.setVarible(variable2,new Complex(m,0));
                double x=compiler.calculate().Re();
                double y=compiler2.calculate().Re();
                double z=compiler3.calculate().Re();
                vertices[currentIndex++]=(float)x;
                vertices[currentIndex++]=(float)y;
                vertices[currentIndex++]=(float)z;
                sendprogress(i*2/3+"%");
                if(i==0&&j==0){
                    xMin=x;
                    xMax=x;
                    yMin=y;
                    yMax=y;
                    zMin=z;
                    zMax=z;
                }
                else{
                    xMin = (xMin<=x)?xMin:x;
                    xMax = (xMax>=x)?xMax:x;
                    yMin = (yMin<=y)?yMin:y;
                    yMax = (yMax>=y)?yMax:y;
                    zMin = (zMin<=z)?zMin:z;
                    zMax = (zMax>=z)?zMax:z;
                }

            }
        }
//        for(int i=0;i<=100;i++){
//            for(int j=0;j<=100;j++){
//                float x=((float)i);
//                float y=((float)j);
//                s.setCoordinate(i,j,(float)Math.sin(x*y/300)*2);
//                sendprogress(i+"%");
//            }
//        }
        for(int i=0;i<=vertices.length-3;i+=3){
            vertices[i]-=xMin;
            vertices[i+1]-=yMin;
            vertices[i+2]-=zMin;
            if(xMax!=xMin)vertices[i]*=(float)(10/(xMax-xMin));
            if(yMax!=yMin)vertices[i+1]*=(float)(10/(yMax-yMin));
            if(zMax!=zMin)vertices[i+2]*=(float)(10/(zMax-zMin));
        }
        EditableMesh frame=new EditableMesh();
        frame.setType("lines");
        //0
        frame.pushvertex(0,10,0);//1
        frame.pushvertex(10,10,0);//2
        frame.pushvertex(10,0,0);//3

        frame.pushvertex(0,0,10);//4
        frame.pushvertex(0,10,10);//5
        frame.pushvertex(10,10,10);//6
        frame.pushvertex(10,0,10);//7

        frame.setindices(new short[]{1,2,0,1,3,2,3,0,4,5,5,6,6,7,7,4,0,4,1,5,2,6,3,7});
        s.setVertex(vertices);
        s.x=-5;
        s.y=-5;
        s.z=-5;
        s.setMaterialSpecular(new float[]{1,1,1,1});
        s.setMaterialDiffuse(new float[]{0,1,0,0.7f});
        frame.x=-5;
        frame.y=-5;
        frame.z=-5;
        renderer.cameraZ=30;
        renderer.scene.setClearColor(1,1,1,1);
        renderer.light0.setPosition(0,0,10,1);
        renderer.scene.add(s);
        renderer.scene.add(frame);
        renderer.light0.setTwoSidesEnabled(true);
        return renderer;
    }
    private OpenGLRenderer plot3dline(String formula1,String formula2,String formula3,String variable,double v1Start,double v1End) throws  Thouway_away{
        double xMin=0,xMax=0,yMin=0,yMax=0,zMin=0,zMax=0;
        FormulaCompiler compiler2=(FormulaCompiler)compiler.clone();
        FormulaCompiler compiler3=(FormulaCompiler)compiler.clone();
        compiler.compile(formula1);
        compiler2.compile(formula2);
        compiler3.compile(formula3);
        EditableMesh line=new EditableMesh();
        line.setType("line_strip");
        int length=300;
        float[] vertices=new float[length*3];
        short[] indices=new short[length];
        for(int i=0;i<length;i++){
            double p=v1Start+(v1End-v1Start)/length*(float) i;
            compiler.setVarible(variable,new Complex(p,0));
            compiler2.setVarible(variable,new Complex(p,0));
            compiler3.setVarible(variable,new Complex(p,0));
            double x=compiler.calculate().Re();
            double y=compiler2.calculate().Re();
            double z=compiler3.calculate().Re();
            vertices[i*3]=(float)x;
            vertices[i*3+1]=(float)y;
            vertices[i*3+2]=(float)z;
            indices[i]=(short)i;
            if(i==0){
                xMin=x;
                xMax=x;
                yMin=y;
                yMax=y;
                zMin=z;
                zMax=z;
            }
            else{
                xMin = (xMin<=x)?xMin:x;
                xMax = (xMax>=x)?xMax:x;
                yMin = (yMin<=y)?yMin:y;
                yMax = (yMax>=y)?yMax:y;
                zMin = (zMin<=z)?zMin:z;
                zMax = (zMax>=z)?zMax:z;
            }
            sendprogress(i/length*100+"");
        }
        for(int i=0;i<=vertices.length-3;i+=3){
            vertices[i]-=xMin;
            vertices[i+1]-=yMin;
            vertices[i+2]-=zMin;
            if(xMax!=xMin)vertices[i]*=(float)(10/(xMax-xMin));
            if(yMax!=yMin)vertices[i+1]*=(float)(10/(yMax-yMin));
            if(zMax!=zMin)vertices[i+2]*=(float)(10/(zMax-zMin));
        }
        line.setvertices(vertices);
        line.setindices(indices);
        EditableMesh frame=new EditableMesh();
        frame.setType("lines");
        //0
        frame.pushvertex(0,10,0);//1
        frame.pushvertex(10,10,0);//2
        frame.pushvertex(10,0,0);//3

        frame.pushvertex(0,0,10);//4
        frame.pushvertex(0,10,10);//5
        frame.pushvertex(10,10,10);//6
        frame.pushvertex(10,0,10);//7

        frame.setindices(new short[]{1,2,0,1,3,2,3,0,4,5,5,6,6,7,7,4,0,4,1,5,2,6,3,7});
        frame.x=-5;
        frame.y=-5;
        frame.z=-5;
        line.x=-5;
        line.y=-5;
        line.z=-5;
        line.setColors(new float[]{0,1,0,1});
        frame.setColors(new float[]{0,0,0,0});
        OpenGLRenderer renderer=new OpenGLRenderer(null);
        renderer.cameraZ=30;
        renderer.scene.setClearColor(1,1,1,1);
        renderer.light0.setPosition(0,0,10,1);
        renderer.scene.add(line);
        renderer.scene.add(frame);
        renderer.light0.setTwoSidesEnabled(true);
        return renderer;
    }
    private float[] HSBtoRGB(float h,float s,float v){
        float[] rgb= new float[3];
        //先令饱和度和亮度为100%，调节色相h
        for(int offset=240,i=0;i<3;i++,offset-=120) {
            //算出色相h的值和三个区域中心点(即0°，120°和240°)相差多少，然后根据坐标图按分段函数算出rgb。但因为色环展开后，红色区域的中心点是0°同时也是360°，不好算，索性将三个区域的中心点都向右平移到240°再计算比较方便
            float x=Math.abs((h+offset)%360-240);
            //如果相差小于60°则为255
            if(x<=60) rgb[i]=255;
                //如果相差在60°和120°之间，
            else if(60<x && x<120) rgb[i]=((1-(x-60)/60)*255);
                //如果相差大于120°则为0
            else rgb[i]=0;
        }
        //在调节饱和度s
        for(int i=0;i<3;i++)
            rgb[i]+=(255-rgb[i])*(1-s);
        //最后调节亮度b
        for(int i=0;i<3;i++)
            rgb[i]*=v;
        return rgb;
    }
    private int getColor(Complex a){
        double modul=Complex.cabs(a).Re();
        double angle=(modul==0)?0:(Math.atan2(a.Im(), a.Re()));
        float s=(float) (Math.exp(-modul/10));
        float b=(float) (1-Math.exp(-modul*30));
        float h=(float) (angle*180/Math.PI);
        h+=(h<0)?(360):(0);
        float[] rgb=HSBtoRGB(h, s, b);
        return Color.rgb((int) (rgb[0]),(int) (rgb[1]),(int)(rgb[2]));
    }
    private void sendprogress(String s){
        if(s!=null)
            if(this.a instanceof MainActivity){
                Message meg=new Message();
                meg.what=1;
                meg.obj=s;
                ((MainActivity) a).handler.sendMessage(meg);
                //((MainActivity) a).changetext(progresstext, progresstext.getText()+s);
                //this.progresstext.append(s);
            }
    }
    private Bitmap addScaleLabel(Bitmap input,float xMin,float xMax,float yMin,float yMax){
        Bitmap output=Bitmap.createBitmap(input.getWidth()+40,input.getHeight()+40,Config.ARGB_8888);
        Canvas canvas=new Canvas(output);
        Paint paint=new Paint();
        paint.setColor(0xa000ff);
        paint.setAlpha(255);
        canvas.drawBitmap(input, 20, 20, paint);
        paint.setTextSize(15);
        canvas.drawText(xMin+"",20,input.getHeight()+30,paint);
        canvas.drawText(xMax+"",input.getWidth()-1,input.getHeight()+30,paint);
        canvas.drawText(yMin+"",0,input.getHeight()+20,paint);
        canvas.drawText(yMax+"",10,20,paint);
        return output;
    }
}
