package eu.nazgee.game.utils.loadable;



public abstract class LoadableResourceScene extends LoadableResource implements ILoadableResourceScene, ILoadingHandlerResourceScene {
	@Override
	abstract public LoaderResourceScene getLoader();
}
