package gameLogic.characters;

import gameLogic.attributes.Status;
import gameLogic.buffs.Buff;
import gameLogic.skills.Skill;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Character {
    private HashMap<String, Status> status = new HashMap<>();
    private HashMap<String, Skill> skills = new HashMap<>();
    private List<Buff> activeBuffs = new ArrayList<>();

    public void addBuff(Buff addedBuff) {
        addedBuff.onApplied();
        if (!addedBuff.isExpired()) {
            activeBuffs.add(addedBuff);
        }
    }

    public List<Buff> getActiveBuffs() {
        return activeBuffs;
    }

    public void cleanBuffList(){
         activeBuffs.removeIf((Buff buff)->{
             if (buff.isExpired()) {
               buff.onRemoved();
               return true;
           } else return false;
        });
    }

    public void changeStatus(String statusName, double amount){
        status.get(statusName).changeValueBy(amount);
    }

    public void applyBuffs(String usingSpecificMethod){
        String methodName = usingSpecificMethod;
        activeBuffs.forEach((Buff buff)->{
            try {
                Method method = buff.getClass().getMethod(methodName);
                method.invoke(buff);
            } catch (SecurityException e) { throw new RuntimeException(e);
            } catch (NoSuchMethodException e) { throw new RuntimeException(e);
            } catch (IllegalArgumentException e) { throw new RuntimeException(e);
            } catch (IllegalAccessException e) { throw new RuntimeException(e);
            } catch (InvocationTargetException e) { throw new RuntimeException(e); }
        });
    }
}
