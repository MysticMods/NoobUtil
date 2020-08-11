package noobanidus.libs.noobutil.capability;

public class EntityCooldownCapability {
  private long cooldown = 0;

  public boolean canHarvest() {
    return cooldown <= System.currentTimeMillis();
  }

  public long getCooldown() {
    return cooldown;
  }

  /**
   * @param cooldown Cooldown in ticks converted to milliseconds
   */
  public void setCooldown(long cooldown) {

    cooldown = (cooldown / 20) * 1000;
    this.cooldown = System.currentTimeMillis() + cooldown;
  }

  public void setActualCooldown(long cooldown) {
    this.cooldown = cooldown;
  }
}
