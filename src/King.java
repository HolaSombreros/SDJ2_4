import utility.collection.ArrayList;
import utility.collection.ListADT;

import java.util.concurrent.ThreadLocalRandom;

public class King implements Runnable {
    private TreasureRoomDoor door;
    
    public King(TreasureRoomDoor door) {
        this.door = door;
    }
    
    @Override
    public void run() {
        while (true) {
            int randomAmount = getRandomNumber(50, 150);
            int total = 0;
            door.acquireWrite();
            ListADT<Valuable> valuables = new ArrayList<>();
            while(door.getValuables().size() > 0) {
                Valuable valuable = door.retrieve();
                total += valuable.getWorth();
                valuables.add(valuable);
                if (total >= randomAmount) {
                    break;
                }
            }
            if (total < randomAmount) {
                Log.getInstance().addLog(Thread.currentThread().getName() + " only collected $" + total + " worth of treasury, which was not enough valuables to throw a party...");
                for (int i = 0; i < valuables.size(); i++)
                    door.addValuable(valuables.get(i));
            }
            else
                Log.getInstance().addLog(Thread.currentThread().getName() + " is throwing a party with treasury worth $" + total + "!");
            door.releaseWrite();
            sleep();
        }
    }
    
    private int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }
    
    private void sleep() {
        int period = ThreadLocalRandom.current().nextInt(8000, 12000);
        try{
            Thread.sleep(period);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
