package com.github.spigot_gillesm.lib_test.profession;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProfessionManager {

	public final Profession BLACKSMITH = new Profession(ProfessionType.BLACKSMITH);
	public final Profession ENCHANTER = new Profession(ProfessionType.ENCHANTER);
	public final Profession ALCHEMIST = new Profession(ProfessionType.ALCHEMIST);
	public final Profession BARMAN = new Profession(ProfessionType.BARMAN);
	public final Profession JOURNALIST = new Profession(ProfessionType.JOURNALIST);
	public final Profession FARMER = new Profession(ProfessionType.FARMER);
	public final Profession PRIEST = new Profession(ProfessionType.PRIEST);
	public final Profession DOCTOR = new Profession(ProfessionType.DOCTOR);
	public final Profession POLICEMAN = new Profession(ProfessionType.POLICEMAN);

}
