package com.smart.aspectj.advanced;

import com.smart.Monitorable;
import com.smart.Waiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class TestAspect {
	//-------------复合运算----------
	@Before("!target(com.smart.NaiveWaiter) && execution(* serveTo(..)))")
	public void notServeInNaiveWaiter() {
		System.out.println("--notServeInNaiveWaiter() executed!--");
	}

	@After("within(com.smart.*)  && execution(* greetTo(..)))")
	public void greeToFun() {
		System.out.println("--greeToFun() executed!--");
	}

	@AfterReturning("target(com.smart.Waiter) || target(com.smart.Seller)")
	public void waiterOrSeller(){
		System.out.println("--waiterOrSeller() executed!--");
	}

	//------------引用命名切点---------- TestNamePointcut.java   //
	@Before("TestNamePointcut.inPkgGreetTo()")
	public void pkgGreetTo(){
		System.out.println("--pkgGreetTo() executed!--");
	}

	@Before("!target(com.smart.NaiveWaiter) && TestNamePointcut.inPkgGreetTo()")
	public void pkgGreetToNotNaiveWaiter(){
		System.out.println("--pkgGreetToNotNaiveWaiter() executed!--");
	}

    //------------访问连接点对象----------//
	@Around("execution(* greetTo(..)) && target(com.smart.NaiveWaiter)") //环绕增强
	public void joinPointAccess(ProceedingJoinPoint pjp) throws Throwable{ //声明连接点入参
		System.out.println("------joinPointAccess-------");
		System.out.println("args[0]:"+pjp.getArgs()[0]);
		System.out.println("signature:"+pjp.getTarget().getClass());
		//通过连接点执行目标对象的方法
		pjp.proceed();
		System.out.println("-------joinPointAccess-------");
	}

  //------------绑定连接点参数----------//
	//args中的参数名称与该函数的参数列表的名称一一对应,增强方法可以通过name和num访问到连接点的方法入参
	@Before("target(com.smart.NaiveWaiter) && args(name,num,..)")
	public void bindJoinPointParams(int num,String name){
	   System.out.println("----bindJoinPointParams()----");
	   System.out.println("name:"+name);
	   System.out.println("num:"+num);
	   System.out.println("----bindJoinPointParams()----");
	}

  //------------绑定代理对象----------//
	//@Before("execution(* greetTo(..)) && this(waiter)")
	//通过bindProxyObj(..)函数的参数列表 获取到this()中的waiter的具体类型为com.smart.Waiter,也是同名入参的增强方式
	@Before("this(waiter)")
	public void bindProxyObj(Waiter waiter){
	   System.out.println("----bindProxyObj()----");
	   System.out.println(waiter.getClass().getName());
       System.out.println("----bindProxyObj()----");
	}

	  //------------绑定类标注对象----------//
	@Before("@within(m)")
	public void bindTypeAnnoObject(Monitorable m){
	   System.out.println("----bindTypeAnnoObject()----");
	   System.out.println(m.getClass().getName());
	   System.out.println("----bindTypeAnnoObject()----");
	}
    //------------绑定抛出的异常----------//
	@AfterReturning(value="target(com.smart.SmartSeller)",returning="retVal")
	public void bingReturnValue(int retVal){
	   System.out.println("----bingReturnValue()----");
	   System.out.println("returnValue:"+retVal);
	   System.out.println("----bingReturnValue()----");
	}

    //------------绑定抛出的异常----------//
	@AfterThrowing(value="target(com.smart.SmartSeller)",throwing="iae")
	public void bindException(IllegalArgumentException iae){
	   System.out.println("----bindException()----");
	   System.out.println("exception:"+iae.getMessage());
	   System.out.println("----bindException()----");
	}
}
