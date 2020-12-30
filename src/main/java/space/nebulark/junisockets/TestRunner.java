package space.nebulark.junisockets;

import org.apache.log4j.BasicConfigurator;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import space.nebulark.junisockets.addresses.IPAddressTest;
import space.nebulark.junisockets.addresses.TCPAddressTest;
import space.nebulark.junisockets.operations.AcceptTest;

public class TestRunner {
   
    public static void main(String[] args) {
        BasicConfigurator.configure();
        Result result = JUnitCore.runClasses(IPAddressTest.class);
        Result result2 = JUnitCore.runClasses(TCPAddressTest.class);
        Result result3 = JUnitCore.runClasses(AcceptTest.class);

        for(Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        for (Failure failure : result2.getFailures()) {
            System.out.println(failure.toString());
        }

        for (Failure failure : result3.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
        System.out.println(result2.wasSuccessful());
        System.out.println(result3.wasSuccessful());
    }
}