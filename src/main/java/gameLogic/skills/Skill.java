package gameLogic.skills;

import gameLogic.buffs.Buff;
import gameLogic.buffs.ClassicalBuff;
import gameLogic.characters.Character;

import java.util.ArrayList;
import java.util.List;

public class Skill {
    private List<Buff> buffs = new ArrayList<>();

    private Character user;
    private Character opponent;
    private int cooldownInTicks;
    private boolean canUse = true;

    public Skill(Character user, Character opponent, int cooldownInTicks) {
        this.user = user;
        this.opponent = opponent;
        this.cooldownInTicks = cooldownInTicks;
    }

    public void use(Character user, Character opponent){
        for (Buff buff : buffs) {

        }
    }

    public boolean canUse(){
        return canUse;
    }

    public void onTick(){
        if (cooldownInTicks > 0){
            cooldownInTicks--;
        } else {
            canUse = true;
        }
    }

    public void linkBuff(){

    }
}
