package eu.nazgee.game.utils.loadable;


public abstract class LoadableResourceSimple extends LoadableResource {

	private final LoaderResource mLoader = new LoaderResource(this);

	@Override
	public LoaderResource getLoader() {
		return mLoader;
	}
}
