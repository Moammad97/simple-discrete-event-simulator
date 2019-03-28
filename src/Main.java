

class Main 
{
    public static void main(String argv[])
    {
        Simulator simulator = new Simulator();
        String file_name = null;
        try
        {   
            
            file_name = argv[0];
            simulator.scan(argv[0]);
            
        }
        catch(Exception e)
        {
            System.out.println("Error in reading input file.");
        }
        simulator.print();
        if (simulator.simulateAll() == -1)
        {
            System.out.println("Simulation failed.");
        }
      
        
    }
}