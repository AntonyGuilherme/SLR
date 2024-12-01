package fr.tp.inf112.projects.robotsim.app;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.tp.inf112.projects.canvas.controller.CanvasViewerController;
import fr.tp.inf112.projects.canvas.controller.Observer;
import fr.tp.inf112.projects.canvas.model.Canvas;
import fr.tp.inf112.projects.canvas.model.CanvasPersistenceManager;
import fr.tp.inf112.projects.robotsim.model.Factory;
import fr.tp.inf112.projects.robotsim.model.FactorySerialyzer;
import fr.tp.inf112.projects.robotsim.model.RemoteFactoryPersistenceManager;

public class RemoteSimulationController implements CanvasViewerController {

	private Factory factoryModel;

	private final CanvasPersistenceManager persistenceManager;

	private final SimulationClient client;
	
	private AtomicBoolean simulationStarted = new AtomicBoolean(false);

	public RemoteSimulationController(Factory remoteFactory, 
			SimulationClient client,
			RemoteFactoryPersistenceManager remoteFactoryPersistenceManager) {
				this.persistenceManager = remoteFactoryPersistenceManager;
				this.client = client;
				this.setCanvas(remoteFactory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObserver(final Observer observer) {
		if (factoryModel != null) {
			return factoryModel.addObserver(observer);
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeObserver(final Observer observer) {
		if (factoryModel != null) {
			return factoryModel.removeObserver(observer);
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCanvas(final Canvas canvasModel) {
		if (factoryModel == null) { 
			this.factoryModel = (Factory) canvasModel;
			return;
		}
		
		List<Observer> observers = null;
		
		observers = factoryModel.getObservers();

		factoryModel = (Factory) canvasModel;
	
		for (final Observer observer : observers) {
			factoryModel.addObserver(observer);
		}
		
		factoryModel.notifyObservers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Canvas getCanvas() {
		return factoryModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startAnimation() {

		setCanvas(client.getFactory());
		
		client.startSimulation();
		
		this.simulationStarted.set(true);
		
		new Thread(() -> this.updateFactory()).start();
	}
	
	private void updateFactory() {
		while(this.simulationStarted.get()) {
			setCanvas(client.getFactory());
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stopAnimation() {
		client.startSimulation();
		
		setCanvas(client.getFactory());
		
		this.simulationStarted.set(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAnimationRunning() {
		return factoryModel != null && factoryModel.isSimulationStarted();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CanvasPersistenceManager getPersistenceManager() {
		return persistenceManager;
	}
}