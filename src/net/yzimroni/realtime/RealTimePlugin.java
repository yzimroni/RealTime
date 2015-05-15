package net.yzimroni.realtime;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class RealTimePlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		initTimeSetter();
		System.out.println("[RealTime] enabled");
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		System.out.println("[RealTime] disabled");
	}

	private void initTimeSetter() {
		for (String name : getConfig().getStringList("worlds")) {
			World world = Bukkit.getWorld(name);
			if (world != null) {
				world.setGameRuleValue("doDaylightCycle", "false");
			}
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				Calendar calendar = GregorianCalendar.getInstance();
				calendar.setTimeZone(TimeZone.getTimeZone(getConfig().getString("timezone")));
				calendar.setTime(new Date());
				double hour = calendar.get(Calendar.HOUR_OF_DAY) + ((double) calendar.get(Calendar.MINUTE) / 60);
				int time = (int) ((hour - 6) * 1000);
				if (time < 0) time += 24000;
				for (String name : getConfig().getStringList("worlds")) {
					World world = Bukkit.getWorld(name);
					if (world != null) {
						world.setTime(time);
					}
				}
			}

		}, 5 * 20, 5 * 60 * 20);
	}

}
