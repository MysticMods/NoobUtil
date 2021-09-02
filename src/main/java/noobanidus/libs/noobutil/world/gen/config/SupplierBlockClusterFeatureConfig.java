package noobanidus.libs.noobutil.world.gen.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.IFeatureConfig;
import noobanidus.libs.noobutil.types.LazyStateSupplier;
import noobanidus.libs.noobutil.types.LazySupplier;
import noobanidus.libs.noobutil.world.gen.provider.AbstractSupplierBlockStateProvider;

public class SupplierBlockClusterFeatureConfig implements IFeatureConfig {
  public static final Codec<SupplierBlockClusterFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(LazyStateSupplier.CODEC.fieldOf("state_provider").forGetter((o) -> o.stateProvider), BlockPlacer.CODEC.fieldOf("block_placer").forGetter((o) -> o.blockPlacer), LazyStateSupplier.CODEC.listOf().fieldOf("whitelist").forGetter((o) -> ImmutableList.copyOf(o.whitelist)), LazyStateSupplier.CODEC.listOf().fieldOf("blacklist").forGetter((o) -> ImmutableList.copyOf(o.blacklist)), Codec.INT.fieldOf("tries").orElse(128).forGetter((o) -> o.tryCount), Codec.INT.fieldOf("xspread").orElse(7).forGetter((o) -> o.xSpread), Codec.INT.fieldOf("yspread").orElse(3).forGetter((o) -> o.ySpread), Codec.INT.fieldOf("zspread").orElse(7).forGetter((o) -> o.zSpread), Codec.BOOL.fieldOf("can_replace").orElse(false).forGetter((o) -> o.isReplaceable), Codec.BOOL.fieldOf("project").orElse(true).forGetter((o) -> o.project), Codec.BOOL.fieldOf("need_water").orElse(false).forGetter((o) -> o.requiresWater)).apply(instance, SupplierBlockClusterFeatureConfig::new));

  public final LazyStateSupplier stateProvider;
  public final BlockPlacer blockPlacer;
  public final Set<LazyStateSupplier> whitelist;
  public final Set<LazyStateSupplier> blacklist;
  public final Set<Block> whitelistResolved = new HashSet<>();
  public final Set<BlockState> blacklistResolved = new HashSet<>();
  public final int tryCount;
  public final int xSpread;
  public final int ySpread;
  public final int zSpread;
  public final boolean isReplaceable;
  public final boolean project;
  public final boolean requiresWater;

  private SupplierBlockClusterFeatureConfig(LazyStateSupplier stateProvider, BlockPlacer blockPlacer, List<LazyStateSupplier> whitelist, List<LazyStateSupplier> blacklist, int tries, int xSpread, int ySpread, int zSpread, boolean isReplaceable, boolean project, boolean requiresWater) {
    this(stateProvider, blockPlacer, ImmutableSet.copyOf(whitelist), ImmutableSet.copyOf(blacklist), tries, xSpread, ySpread, zSpread, isReplaceable, project, requiresWater);
  }

  private SupplierBlockClusterFeatureConfig(LazyStateSupplier stateProvider, BlockPlacer blockPlacer, Set<LazyStateSupplier> whitelist, Set<LazyStateSupplier> blacklist, int tries, int xSpread, int ySpread, int zSpread, boolean isReplaceable, boolean project, boolean requiresWater) {
    this.stateProvider = stateProvider;
    this.blockPlacer = blockPlacer;
    this.whitelist = whitelist;
    this.blacklist = blacklist;
    this.tryCount = tries;
    this.xSpread = xSpread;
    this.ySpread = ySpread;
    this.zSpread = zSpread;
    this.isReplaceable = isReplaceable;
    this.project = project;
    this.requiresWater = requiresWater;
  }

  public Set<Block> getWhitelist() {
    if (whitelistResolved.isEmpty() && !whitelist.isEmpty()) {
      whitelist.stream().map(LazyStateSupplier::get).map(BlockState::getBlock).forEach(whitelistResolved::add);
    }
    return whitelistResolved;
  }

  public Set<BlockState> getBlacklist() {
    if (blacklistResolved.isEmpty() && !blacklist.isEmpty()) {
      blacklist.stream().map(LazySupplier::get).forEach(blacklistResolved::add);
    }
    return blacklistResolved;
  }

  public static class Builder {
    private final LazyStateSupplier stateProvider;
    private final BlockPlacer blockPlacer;
    private Set<LazyStateSupplier> whitelist = ImmutableSet.of();
    private Set<LazyStateSupplier> blacklist = ImmutableSet.of();
    private int tryCount = 64;
    private int xSpread = 7;
    private int ySpread = 3;
    private int zSpread = 7;
    private boolean isReplaceable;
    private boolean project = true;
    private boolean requiresWater = false;

    public Builder(LazyStateSupplier stateProvider, BlockPlacer blockPlacer) {
      this.stateProvider = stateProvider;
      this.blockPlacer = blockPlacer;
    }

    public SupplierBlockClusterFeatureConfig.Builder whitelist(Set<LazyStateSupplier> whitelist) {
      this.whitelist = whitelist;
      return this;
    }

    public SupplierBlockClusterFeatureConfig.Builder blacklist(Set<LazyStateSupplier> blacklist) {
      this.blacklist = blacklist;
      return this;
    }

    public SupplierBlockClusterFeatureConfig.Builder tries(int tries) {
      this.tryCount = tries;
      return this;
    }

    public SupplierBlockClusterFeatureConfig.Builder xSpread(int xSpread) {
      this.xSpread = xSpread;
      return this;
    }

    public SupplierBlockClusterFeatureConfig.Builder ySpread(int ySpread) {
      this.ySpread = ySpread;
      return this;
    }

    public SupplierBlockClusterFeatureConfig.Builder zSpread(int zSpread) {
      this.zSpread = zSpread;
      return this;
    }

    public SupplierBlockClusterFeatureConfig.Builder replaceable() {
      this.isReplaceable = true;
      return this;
    }

    public SupplierBlockClusterFeatureConfig.Builder noProjection() {
      this.project = false;
      return this;
    }

    public SupplierBlockClusterFeatureConfig.Builder requiresWater() {
      this.requiresWater = true;
      return this;
    }

    public SupplierBlockClusterFeatureConfig build() {
      return new SupplierBlockClusterFeatureConfig(this.stateProvider, this.blockPlacer, this.whitelist, this.blacklist, this.tryCount, this.xSpread, this.ySpread, this.zSpread, this.isReplaceable, this.project, this.requiresWater);
    }
  }
}

