/*
 * $Id$
 */
package org.jnode.test.core;

import java.io.IOException;
import java.io.InterruptedIOException;
import javax.isolate.Isolate;
import javax.isolate.IsolateStartupException;
import javax.isolate.IsolateStatus;
import javax.isolate.Link;
import javax.isolate.LinkMessage;

public class StatusLinkTest {

    public static void main(String[] args) throws IsolateStartupException, InterruptedIOException, IOException {
        String clsName = ChildClass.class.getName();
        Isolate child = new Isolate(clsName);
        Link link = child.newStatusLink();
        new Thread(new StatusMonitor(link)).start();
        child.start();
    }

    public static class StatusMonitor implements Runnable {
        private final Link link;

        public StatusMonitor(Link link) {
            this.link = link;
        }

        public void run() {
            try {
                while (true) {
                    LinkMessage msg = link.receive();
                    if (msg.containsStatus()) {
                        IsolateStatus is = msg.extractStatus();
                        if (is.getState().equals(IsolateStatus.State.EXITED)) {
                            System.out.println("Message: state=" + is.getState() + " code=" + is.getExitCode() +
                                " reason=" + is.getExitReason());
                            break;
                        } else {
                            System.out.println("Message: state=" + is.getState());
                        }
                    } else {
                        System.out.println("Unknown message: " + msg);
                    }
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }

    public static class ChildClass {

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Child: started");
            System.out.println("Child: sleeping 3 seconds");
            Thread.sleep(3000);
            System.out.println("Child: exiting");
        }
    }
}
