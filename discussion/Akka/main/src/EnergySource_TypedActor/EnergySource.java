package EnergySource_TypedActor;

public interface EnergySource
{
  long getUnitsAvailable();

  long getUsageCount();

  void useEnergy( final long units );
}
