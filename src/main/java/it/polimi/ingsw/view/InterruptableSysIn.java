package it.polimi.ingsw.view;
import java.util.concurrent.Semaphore;
import java.util.Scanner;

    public class InterruptableSysIn {
        protected static Scanner input = new Scanner (System.in);
        protected static final Semaphore waitingForInput = new Semaphore(0,true); //If InterruptableSysIn is waiting on input.nextLine(); Can also be cleared by cancel();
        protected static String currentLine = ""; //What the last scanned-in line is
        private static final Input inputObject = new Input();
        private static final Semaphore waitingOnOutput = new Semaphore (1); // If there's someone waiting for output. Used for thread safety
        private static boolean canceled = false; //If the last input request was cancled.
        private static boolean ignoreNextLine = false; //If the last cancel() call indicated input should skip the next line.
        private static final String INTERRUPTED_ERROR = "\nInterrupted";
        private static final String INUSE_ERROR = "\nInUse";
        private static boolean lasLineInterrupted = false;

        /**
         * This method will block if someone else is already waiting on a next line.
         * Gaurentees on fifo order - threads are paused, and enter a queue if the
         * input is in use at the time of request, and will return in the order the
         * requests were made
         * @return The next line from System.in, or "\nInterrupted" if it's interrupted for any reason
         */
        public static String nextLineBlocking(){
            //Blocking portion
            try{
                waitingOnOutput.acquire(1);
            }catch(InterruptedException iE){
                return INTERRUPTED_ERROR;
            }
            String toReturn = getNextLine();
            waitingOnOutput.release(1);
            return toReturn;
        }

        /**
         * This method will immediately return if someone else is already waiting on a next line.
         * @return The next line from System.in, or
         * "\nInterrupted" if it's interrupted for any reason
         * "\nInUse" if the scanner is already in use
         */
        public static String nextLineNonBlocking(){
            //Failing-out portion
            if(!waitingOnOutput.tryAcquire(1)){
                return INUSE_ERROR;
            }
            String toReturn = getNextLine();
            waitingOnOutput.release(1);
            return toReturn;
        }

        /**
         * This method will block if someone else is already waiting on a next line.
         * Gaurentees on fifo order - threads are paused, and enter a queue if the
         * input is in use at the time of request, and will return in the order the
         * requests were made
         * @param ignoreLastLineIfUnused If the last line was canceled or Interrupted, throw out that line, and wait for a new one.
         * @return The next line from System.in, or "\nInterrupted" if it's interrupted for any reason
         */
        public static String nextLineBlocking(boolean ignoreLastLineIfUnused){
            ignoreNextLine = ignoreLastLineIfUnused;
            return nextLineBlocking();
        }

        /**
         * This method will fail if someone else is already waiting on a next line.
         * @param ignoreLastLineIfUnused If the last line was canceled or Interrupted, throw out that line, and wait for a new one.
         * @return The next line from System.in, or
         * "\nInterrupted" if it's interrupted for any reason
         * "\nInUse" if the scanner is already in use
         */
        public static String nextLineNonBlocking(boolean ignoreLastLineIfUnused){
            ignoreNextLine = ignoreLastLineIfUnused;
            return nextLineNonBlocking();
        }

        private static String getNextLine(){
            String toReturn = currentLine; //Cache the current line on the very off chance that some other code will run etween the next few lines

            if(canceled){//If the last one was cancled
                canceled = false;

                //If there has not been a new line since the cancelation
                if (toReturn.equalsIgnoreCase(INTERRUPTED_ERROR)){
                    //If the last request was cancled, and has not yet recieved an input

                    //wait for that input to finish
                    toReturn = waitForLineToFinish();
                    //If the request to finish the last line was interrupted
                    if(toReturn.equalsIgnoreCase(INTERRUPTED_ERROR)){
                        return INTERRUPTED_ERROR;
                    }

                    if(ignoreNextLine){
                        //If the last line is supposed to be thrown out, get a new one
                        ignoreNextLine = false;
                        //Request an input
                        toReturn = getLine();
                    }else{
                        return toReturn;
                    }

                    //If there has been a new line since cancelation
                }else{
                    //If the last request was cancled, and has since recieved an input
                    try{
                        waitingForInput.acquire(1); //Remove the spare semaphore generated by having both cancel() and having input
                    }catch(InterruptedException iE){
                        return INTERRUPTED_ERROR;
                    }

                    if(ignoreNextLine){
                        ignoreNextLine = false;
                        //Request an input
                        toReturn = getLine();
                    }
                    //return the last input
                    return toReturn;
                }
            }else{
                if(lasLineInterrupted){

                    //wait for that input to finish
                    toReturn = waitForLineToFinish();
                    //If the request to finish the last line was interrupted
                    if(toReturn.equalsIgnoreCase(INTERRUPTED_ERROR)){
                        return INTERRUPTED_ERROR;
                    }

                    //Should the read be thrown out?
                    if(ignoreNextLine){
                        //Request an input
                        toReturn = getLine();
                    }

                }else{
                    ignoreNextLine = false; //If it's been set to true, but there's been no cancaleation, reset it.

                    //If the last request was not cancled, and has not yet recieved an input
                    //Request an input
                    toReturn = getLine();
                }
            }
            return toReturn;
        }

        private static String getLine (){
            Thread ct = new Thread(inputObject);
            ct.start();
            //Makes this cancelable
            try{
                waitingForInput.acquire(1); //Wait for the input
            }catch(InterruptedException iE){
                lasLineInterrupted = true;
                return INTERRUPTED_ERROR;
            }
            if(canceled){
                return INTERRUPTED_ERROR;
            }
            return currentLine;
        }

        public static String waitForLineToFinish(){
            //If the last request was interrupted
            //wait for the input to finish
            try{
                waitingForInput.acquire(1);
                lasLineInterrupted = false;
                canceled = false;
                return currentLine;
            }catch(InterruptedException iE){
                lasLineInterrupted = true;
                return INTERRUPTED_ERROR;
            }
        }

        /**
         * Cancels the currently waiting input request
         */
        public static void cancel(){
            if(!waitingOnOutput.tryAcquire(1)){ //If there is someone waiting on user input
                canceled = true;
                currentLine = INTERRUPTED_ERROR;
                waitingForInput.release(1); //Let the blocked scanning threads continue, or restore the lock from tryAquire()
            }else{
                waitingOnOutput.release(1); //release the lock from tryAquire()
            }
        }

        public static void cancel(boolean throwOutNextLine){
            if(!waitingOnOutput.tryAcquire(1)){ //If there is someone waiting on user input
                canceled = true;
                currentLine = INTERRUPTED_ERROR;
                ignoreNextLine = throwOutNextLine;
                waitingForInput.release(1); //Let the blocked scanning threads continue
            }else{
                waitingOnOutput.release(1); //release the lock from tryAquire()
            }
        }

    }

    class Input implements Runnable{
        @Override
        public void run (){
            InterruptableSysIn.currentLine = InterruptableSysIn.input.nextLine();
            InterruptableSysIn.waitingForInput.release(1); //Let the main thread know input's been read
        }

    }
}
