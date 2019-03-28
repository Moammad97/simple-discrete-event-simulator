import java.util.*;

import java.io.*;
class Signal
{
    private String name;
    private int value;
    private int lastUpdateTime;
    String wave;
    PrintWriter writer;
    public Signal(String name) throws Exception
    {
        this.setName(name);
        this.setValue(-1,-1);
        this.lastUpdateTime = -1;
        if (this instanceof Output)
        {
            writer = new PrintWriter("../output/" +name+ ".txt");//"UTF-8");
            writer.print(name + ": ");
        }
        
        wave = new String("");

    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return this.name;
    }
    public int getLastUpdateTime()
    {
        return this.lastUpdateTime;
    }
    public boolean setValue(int value,int time)
    {

        if (getValue() != value)
        {
            int i = getLastUpdateTime();
            char underLine = '_';
            char overLine = '-';
             if (time!= 0)
             {   
                for (;i < time;i++)
                {
                   
                    if (getValue()==0)
                    {
                        wave+= underLine;
                    }
                    else if (getValue() ==1)
                    {
                        wave+= overLine;
                    }
                    else
                    {
                        wave +='X';
                    }
                }
                }
           
            this.value = value;
            this.lastUpdateTime = time;
            return true;
        }
        return false;
    }
    public void write(int time)
    {
        if (this instanceof Output)
            writer.print(getValue() + " at "+ time+", ");
    }
    public int getValue()
    {
        return this.value;
    }
    public void close()
    {
        if (this instanceof Output)
        {
            writer.println();
            writer.print(wave);
            writer.close();
        }
    }
    public String getWave()
    {
        return this.wave;
    }
}
class Input extends Signal
{
    public Input(String name) throws Exception
    {
        super(name);
    }
}
class Output extends Signal
{

    public Output(String name) throws Exception
    {
        super(name); 
    }
}
class Internal extends Signal
{
    public Internal(String name) throws Exception
    {
        super(name);
    }
}