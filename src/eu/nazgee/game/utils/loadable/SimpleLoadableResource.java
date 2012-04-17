package eu.nazgee.game.utils.loadable;


public abstract class SimpleLoadableResource extends LoadableResource {

	private final LoaderResource mLoader = new LoaderResource(this);

	@Override
	public LoaderResource getLoader() {
		return mLoader;
	}
}
