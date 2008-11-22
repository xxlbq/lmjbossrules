package com.bestwiz;

import java.io.InputStreamReader;
import java.io.Reader;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.PackageBuilder;
import org.drools.rule.Package;

import cn.bestwiz.jhf.core.jms.bean.OrderBindInfo;

import com.sample.DroolsTest;



public class TraderRulesTest {

    public static final void main(String[] args) {
        try {
        	
        	//load up the rulebase
            RuleBase ruleBase = readRule();
            WorkingMemory workingMemory = ruleBase.newStatefulSession();
            //build orderBindInfo obj
            OrderBindInfo info = getOrderBindInfo();
            
            //go !
           
            workingMemory.insert( info );
            workingMemory.fireAllRules();   
            
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    private static OrderBindInfo getOrderBindInfo() {
		//maybe from JMS
    	// ... ...
    	
    	//mock

		return getOpmOrderManualMode();
	}

    /**
     * opm 滞留模式下
     * @return
     */
    private static OrderBindInfo getOpmOrderStayPositionMode(){
    	
    	OrderBindInfo info =new OrderBindInfo();
    	
    	//opm order 
    	info.setType( 10 );
    	//
    	info.setMode( 1 );
    	info.setCustomerId("lubq");
    	info.setSide( -1 );
    	// 1#  IN_MANUAL  ,2#  NOT_IN_MANUALs
    	info.setInManualStatus( 1 );
    	
    	return info;
    }
    
    
    /**
     * opm 手动汇率
     * @return
     */
    private static OrderBindInfo getOpmOrderManualMode(){
    	
    	OrderBindInfo info =new OrderBindInfo();
    	
    	//opm order 
    	info.setType( 10 );
    	//
    	info.setMode( 1 );
    	info.setCustomerId("lubq");
    	info.setSide( -1 );
    	// 1#  IN_MANUAL  ,2#  NOT_IN_MANUAL
    	info.setInManualStatus( 1 );
    	
    	return info;
    }
    
	/**
     * Please note that this is the "low level" rule assembly API.
     */
	private static RuleBase readRule() throws Exception {
		//read in the source
		Reader source = new InputStreamReader( 
				DroolsTest.class.getResourceAsStream( "/traderMock/rules/trader.drl" ) );
		
		//optionally read in the DSL (if you are using it).
		//Reader dsl = new InputStreamReader( DroolsTest.class.getResourceAsStream( "/mylang.dsl" ) );

		//Use package builder to build up a rule package.
		//An alternative lower level class called "DrlParser" can also be used...
		
		PackageBuilder builder = new PackageBuilder();

		//this wil parse and compile in one step
		//NOTE: There are 2 methods here, the one argument one is for normal DRL.
		builder.addPackageFromDrl( source );

		//Use the following instead of above if you are using a DSL:
		//builder.addPackageFromDrl( source, dsl );
		
		//get the compiled package (which is serializable)
		Package pkg = builder.getPackage();
		
		//add the package to a rulebase (deploy the rule package).
		RuleBase ruleBase = RuleBaseFactory.newRuleBase();
		ruleBase.addPackage( pkg );
		
		
		return ruleBase;
	}
}
