import java.util.*;
import java.io.*;

class Simulator
{
    private boolean isStable;
    private int  maxSimTime;
    private int currentSimTime;
    private ArrayList<Signal> signalList;
    private ArrayList<Gate> gateList;
    private PriorityQueue<Event> eventQueue;
    PrintWriter writer;
    public Simulator()
    {
        signalList = new ArrayList<Signal>();
        gateList = new ArrayList<Gate>();
        eventQueue = new PriorityQueue<Event>(new EventComparator());
    }
    public void scan(String file_name) throws Exception
    {
        File file = new File(file_name);
        writer = new PrintWriter("../Output.txt");

        Scanner scanner = new Scanner(file);
       
        int i =1;
        while (scanner.hasNext())
        {
            System.out.println("At line "+ i++);
            String line = scanner.nextLine();
            setInfo(extract(line));
        }
      
    }
    private void setInfo(ArrayList<String> tokens) throws Exception
    {
        if (tokens.size() ==0) return;
        switch(tokens.get(0))
        {
            case "simtime":
            {
                maxSimTime = Integer.parseInt(tokens.get(1));
                break;
            }
            case "input":
            {
                for (int i = 1; i< tokens.size();i++)
                {
                    signalList.add(new Input(tokens.get(i)));
                }
                break;
            }
            case "signal":
            {
                for (int i = 1; i< tokens.size();i++)
                {
                    signalList.add(new Internal(tokens.get(i)));
                }
                break;
            }
            case "output":
            {
                for (int i = 1; i< tokens.size();i++)
                {
                    signalList.add(new Output(tokens.get(i)));
                }
                break;
            }
            case "not":
            {
                String name = tokens.get(1);
                Signal input = search(tokens.get(2));
                Signal output = search(tokens.get(3));
                int delay = Integer.parseInt(tokens.get(4));
                gateList.add(new Not(name,input,output, delay));
                break;
            }
            case "and":
            {   
                String name = tokens.get(1);
                Signal input1 = search(tokens.get(2));
                Signal input2 = search(tokens.get(3));
                Signal output = search(tokens.get(4));
                int delay = Integer.parseInt(tokens.get(5));
                gateList.add(new And(name,input1, input2,output, delay));
                break;
            }
            case "or":
            {
                
                String name = tokens.get(1);
                Signal input1 = search(tokens.get(2));
                Signal input2 = search(tokens.get(3));
                Signal output = search(tokens.get(4));
                int delay = Integer.parseInt(tokens.get(5));
                gateList.add(new Or(name,input1, input2,output, delay));
                break;
            }
            case "value":
            {
                
                Signal signal = search(tokens.get(1));
                if (signal == null)
                {
                    System.out.println("Can't find signal "+ tokens.get(1));
                    break;
                }
                int time = 0;
                for (int i =2; i <tokens.size(); i+=2)
                {
                    int value = Integer.parseInt(tokens.get(i));
                    int time_after = Integer.parseInt(tokens.get(i+1));
                    time += time_after;
            
                    schedule(new Event(signal, time, value));

                }
                break;
            }
        }
    }
    private ArrayList<String> extract(String line)
    {
        ScannerState state = ScannerState.idle;
        ArrayList<String> tokens = new ArrayList<String>();
        String recentString= "";
        boolean finished = false;
        for (int i = 0 ; i < line.length(); i++)
        {
            char recent = line.charAt(i);
            switch(state)
            {
                case idle:
                {
                    if (isLetter(recent))
                    {
                        recentString += recent;
                        state = ScannerState.readLetter;
                    }
                    else if (isNumber(recent))
                    {
                        recentString += recent;
                        state = ScannerState.readNum;
                    }   
                    else if (recent =='/' && line.charAt(i+1)== '/')
                    {
                        finished = true;
                    }
                    else if (recent == ';')
                    {
                        finished = true;
                    }
                    break;
                }
                case readLetter:
                {
                    if (isLetter(recent) || isNumber(recent))
                    {
                        recentString += recent;
                    }
                    else if (recent == ';')
                    {
                        tokens.add(recentString);
                        recentString = "";
                        finished = true;
                    }
                    else
                    {
                        tokens.add(recentString);
                        recentString = "";
                        state = ScannerState.idle;
                    }
                    break;
                }
                case readNum:
                {
                    if (isNumber(recent))
                    {
                        recentString += recent;
                    }
                    else if (recent == ';')
                    {
                        tokens.add(recentString);
                        recentString = "";
                        finished = true;
                    }
                    else
                    {
                        tokens.add(recentString);
                        recentString = "";
                        state = ScannerState.idle;
                    }
                    break;
                }

            }
            if (finished)
                break;
        }
        return tokens;
    }
    public int simulateAll() 
    {
        currentSimTime = 0;
        while (!eventQueue.isEmpty() && currentSimTime < maxSimTime )
        {
            Event recentEvent = eventQueue.peek();
            currentSimTime = recentEvent.getTime();
            System.out.println("Current Simulation Time = "+ currentSimTime);
            simulate();
        }
        for (int i =0; i < 10;i++)  writer.print(" ");
        for (int i =0; i < currentSimTime+10; i+=5) 
        {
            if (i%5 == 0)
                writer.printf("%4d|",i);
            else 
                writer.print("     ");
        }
            
        for(Signal signal: signalList)
        {
            writer.println();
            writer.print(signal.getName());
            for (int i = signal.getName().length(); i  <12; i++)
            {
                writer.print(" ");
            }
            writer.print(": "+signal.getWave());
            for (int i = signal.getWave().length(); i<= currentSimTime + 5; i++)
            {
                writer.print( (signal.getValue()==0)?'_':'-' );
            }
            
            signal.close();
        }
        writer.close();
        return 0;
    }
    private boolean isLetter(char x)
    {
        if (x <= 'Z' && x >= 'A') return true;
        if (x <= 'z' && x >= 'a') return true;
        return false;
    }
    private boolean isNumber(char x)
    {
        if (x <= '9' && x >= '0') return true;
        return false;
    }
    public void print()
    {
        System.out.println("Signals List:");
        for (Signal signal:signalList)
        {
            System.out.print("\t");
            if(signal instanceof Input)
                System.out.print("input: ");
            if(signal instanceof Output)
                System.out.print("Output: ");
            if(signal instanceof Internal)
                System.out.print("Internal: ");
            System.out.println(signal.getName());
        }
        System.out.println("Gate List:");
        for (Gate gate: gateList)
        {
            System.out.print("\t");
            if(gate instanceof Not)
                System.out.print("Not: ");
            if(gate instanceof And)
                System.out.print("And: ");
            if(gate instanceof Or)
                System.out.print("Or: ");
            System.out.println(gate.getName() +" "+ gate.getOutput().getName());
        }
        System.out.println("Event List:");
        for (Event event: eventQueue)
        {
            System.out.print("\t");
            if (event.getSignal() != null)
                System.out.println("siganl "+ event.getSignal().getName()+
                    " at " +event.getTime() + " is " + event.getValue() ) ;
        }
    }   
    private Signal search(String name)
    {
        for (int i = 0; i<signalList.size(); i++)
        {
            if (signalList.get(i).getName().equals(name))
                return signalList.get(i);
        }
        return null;
    }
    private void simulate()
    {
        Event recEvent = null;
        while (!eventQueue.isEmpty())
        {   
            if (eventQueue.peek().getTime() != currentSimTime)
                break;
            recEvent = eventQueue.poll();
            evaluateAll(recEvent);
        }

        for (Signal signal:signalList)
        {
            if (signal instanceof Output  &&
                 signal.getLastUpdateTime() == currentSimTime)
            {
                signal.write(currentSimTime);
            }
        }
        
        
    }
    private void evaluateAll(Event event)
    {
        Signal signal = event.getSignal();
        boolean changed =signal.setValue(event.getValue(),currentSimTime);
        for (Gate gate: gateList)
        {
            if (gate instanceof Not)
            {
                Not not = (Not)gate;
                if (not.getInput().equals(signal))
                {
                    int preValue = not.getOutput().getValue();
                    Event newEvent = not.evaluate(event);
                    if(newEvent.getTime() == event.getTime() )
                    {
                        if (preValue != newEvent.getValue())
                        {
                            evaluateAll(newEvent);
                        }
                    }
                    else
                    {
                        schedule(newEvent);
                    }

                }
            }
            else if (gate instanceof Or)
            {
                Or or = (Or)gate;
                if (or.getInput1().equals(signal) || or.getInput2().equals(signal) )
                {
                    int preValue = or.getOutput().getValue();
                    Event newEvent = or.evaluate(event);
                    if(newEvent.getTime() == event.getTime() )
                    {
                        if (preValue != newEvent.getValue())
                        {
                            evaluateAll(newEvent);
                        }
                    }
                    else
                    {
                        schedule(newEvent);
                    }
                }
            }
            else if (gate instanceof And)
            {
                And and = (And)gate;
                if (and.getInput1().equals(signal) || and.getInput2().equals(signal) )
                {
                    int preValue = and.getOutput().getValue();
                    Event newEvent = and.evaluate(event);
                    if(newEvent.getTime() == event.getTime() )
                    {
                        if (preValue != newEvent.getValue())
                        {
                            evaluateAll(newEvent);
                        }
                    }
                    else
                    {
                        schedule(newEvent);
                    }
                }
            }

           
        }
    }
    private void schedule(Event event)
    {
        eventQueue.add(event);
    }
}

enum ScannerState 
{
    idle, readLetter, readNum
}
