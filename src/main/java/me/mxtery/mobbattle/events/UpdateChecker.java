package me.mxtery.mobbattle.events;

import me.mxtery.mobbattle.MobBattle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

public class UpdateChecker {
    private int resourceId;
    private URL resourceURL;
    private String currentVersionString;
    private String latestVersionString;
    private UpdateCheckResult updateCheckResult;

    public UpdateChecker(MobBattle plugin, int resourceId) {
        try {
            this.resourceId = resourceId;
            this.resourceURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
        } catch (Exception exception) {
            return;
        }

        currentVersionString = plugin.getDescription().getVersion();
        latestVersionString = getLatestVersion();

        if (latestVersionString == null) {
            updateCheckResult = UpdateCheckResult.NO_RESULT;
            return;
        }

        int currentVersion = Integer.parseInt(currentVersionString.replace("v", "").replace(".", ""));
        int latestVersion = Integer.parseInt(getLatestVersion().replace("v", "").replace(".", ""));

        if (currentVersion < latestVersion) updateCheckResult = UpdateCheckResult.OUT_DATED;
        else if (currentVersion == latestVersion) updateCheckResult = UpdateCheckResult.UP_TO_DATE;
        else updateCheckResult = UpdateCheckResult.UNRELEASED;


        switch (updateCheckResult) {
            case OUT_DATED:
                plugin.getServer().getLogger().log(Level.WARNING, "[MobBattle] 你正在运行过时的 MobBattle版本! 最新版本是 v" + latestVersionString + ".");
                break;
            case UP_TO_DATE:
                plugin.getServer().getLogger().log(Level.INFO, "[MobBattle] 你正在使用最新版 MobBattle! " + "(v" + currentVersionString + ")");
                break;
            case UNRELEASED:
                plugin.getServer().getLogger().log(Level.WARNING, "[MobBattle] 你正在使用开发版本! " + "(v" + currentVersionString + ")");
                break;
        }

    }

    public int getResourceId() {
        return resourceId;
    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + resourceId;
    }

    public String getCurrentVersionString() {
        return currentVersionString;
    }

    public String getLatestVersionString() {
        return latestVersionString;
    }

    public UpdateCheckResult getUpdateCheckResult() {
        return updateCheckResult;
    }

    public String getLatestVersion() {
        try {
            URLConnection urlConnection = resourceURL.openConnection();
            return new BufferedReader(new InputStreamReader(urlConnection.getInputStream())).readLine();
        } catch (Exception exception) {
            return null;
        }
    }

    public enum UpdateCheckResult {
        NO_RESULT, OUT_DATED, UP_TO_DATE, UNRELEASED,
    }
}
