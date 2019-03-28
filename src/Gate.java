
abstract class Gate
{
    private String name;
    private Signal output;
    private int delay;
    public Gate(){}
    public Gate(String name, Signal output, int delay)
    {
        setName(name);
        setOutput(output);
        setDelay(delay);
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return this.name;
    }
    public void setOutput(Signal output)
    {
        this.output = output;
    }
    public Signal getOutput()
    {
        return this.output;
    }
    public void setDelay(int delay)
    {
        this.delay = delay;
    }
    public int getDelay()
    {
        return this.delay;
    }
    public abstract Event evaluate(Event event);
}
class Not extends Gate
{
    private Signal input;
    public Not(String name, Signal input, Signal output,int delay)
    {
        super(name,output,delay);
        setInput(input);
    }
    public void setInput(Signal input)
    {
        this.input = input;
    }
    public Signal getInput()
    {
        return this.input;
    }
    @Override
    public Event evaluate(Event event)
    {
        // this.getOutput().setValue(~event.getValue());

        int value = event.getValue(); 
        if (value == 0) value = 1;
        else if (value == 1) value = 0;

        Event newEvent = new Event(
            this.getOutput(),
            event.getTime() + this.getDelay(),
            value
        );
        return newEvent;
    }
}
class And extends Gate
{
    private Signal input1;
    private Signal input2;
    public And(String name, Signal input1, Signal input2, Signal output,int delay)
    {
        super(name,output,delay);
        setInput1(input1);
        setInput2(input2);
    }
    public void setInput1(Signal input1)
    {
        this.input1 = input1;
    }
    public Signal getInput1()
    {
        return this.input1;
    }
    public void setInput2(Signal input2)
    {
        this.input2 = input2;
    }
    public Signal getInput2()
    {
        return this.input2;
    }
    @Override
    public Event evaluate(Event event)
    {
        int value;
        if(input1.getValue()==1 && input2.getValue()==1)
        {
            value = 1;
        }
        else
        {
            value = 0;
        }

        Event newEvent = new Event(
            super.getOutput(),
            event.getTime() + this.getDelay(),
            value
        );
        return newEvent;
    }
}
class Or extends Gate
{
    private Signal input1;
    private Signal input2;
    public Or(String name, Signal input1, Signal input2, Signal output, int delay)
    {
        super(name,output,delay);
        setInput1(input1);
        setInput2(input2);
    }
    public void setInput1(Signal input1)
    {
        this.input1 = input1;
    }
    public Signal getInput1()
    {
        return this.input1;
    }
    public void setInput2(Signal input2)
    {
        this.input2 = input2;
    }
    public Signal getInput2()
    {
        return this.input2;
    }
    @Override
    public Event evaluate(Event event)
    {
        int value;
        if(input1.getValue()==0 && input2.getValue()==0)
        {
            value = 0;
        }
        else
        {
            value =1;
        }
        Event newEvent = new Event(
            this.getOutput(),
            event.getTime() + this.getDelay(),
            value
        );
        return newEvent;
    }
}