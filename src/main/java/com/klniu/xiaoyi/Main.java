package com.klniu.xiaoyi;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) throws Exception {
        //ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(RobotConfig.class);
        Robot robot = ctx.getBean(Robot.class);
        //       Robot robot = (Robot) context.getBean("robot"); // bean id
        robot.loop();
        //Iat iat = new Iat("58736af3");
        //RecognizerListener recognizerListener = new RecognizerListener() {
        //    @Override
        //    public void onBeginOfSpeech() {
        //        System.out.println("begin");
        //    }

        //    @Override
        //    public void onEndOfSpeech() {
        //        System.out.println("end");
        //    }

        //    @Override
        //    public void onResult(String result, boolean isLast) {
        //        System.out.println("result" + isLast);
        //    }
        //};
        //iat.startListening(recognizerListener);
        //System.out.print(iat.isListening());
        //sleep(5000);
        //iat.stopListening();
    }
}
