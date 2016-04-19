package gameLogic.skills;

import gameLogic.buffs.Buff;
import gameLogic.buffs.BuffMethodsTypes;
import gameLogic.characters.Character;

import java.util.ArrayList;
import java.util.List;

public class Skill {
    private List<Buff> buffs = new ArrayList<>();

    private Character user;
    private Character oponent;

    public Skill(Character oponent, Character user) {
        this.oponent = oponent;
        this.user = user;
    }

    public List<Buff> getBuffs() {
        this.user.applyBuffs(BuffMethodsTypes.ON_APPLIED);
        return buffs;
    }


    //onUse

    //canUse
}
