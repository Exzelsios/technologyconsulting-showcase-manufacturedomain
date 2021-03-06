package de.novatec.showcase.manufacture.ejb.session;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.novatec.showcase.manufacture.ejb.entity.Assembly;
import de.novatec.showcase.manufacture.ejb.entity.Bom;
import de.novatec.showcase.manufacture.ejb.entity.BomPK;
import de.novatec.showcase.manufacture.ejb.entity.Component;
import de.novatec.showcase.manufacture.ejb.entity.ComponentDemand;
import de.novatec.showcase.manufacture.ejb.entity.Inventory;
import de.novatec.showcase.manufacture.ejb.entity.InventoryPK;
import de.novatec.showcase.manufacture.ejb.session.exception.AssemblyNotFoundException;
import de.novatec.showcase.manufacture.ejb.session.exception.BomNotFoundException;
import de.novatec.showcase.manufacture.ejb.session.exception.ComponentNotFoundException;
import de.novatec.showcase.manufacture.ejb.session.exception.InventoryNotFoundException;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ManufactureSession implements ManufactureSessionLocal {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Component findComponent(String componentId) throws ComponentNotFoundException {
		Component component = em.find(Component.class, componentId);
		if (component == null) {
			throw new ComponentNotFoundException("The Component with the id " + componentId + "was not found!");
		}
		return component;
	}

	@Override
	public Collection<Component> getAllComponents() {
		return em.createNamedQuery(Component.ALL_COMPONENTS, Component.class).getResultList();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Assembly findAssembly(String componentId) throws AssemblyNotFoundException {
		Assembly assembly = em.find(Assembly.class, componentId);
		if (assembly == null) {
			throw new AssemblyNotFoundException("The Assembly with the id " + componentId + "was not found!");
		}
		return assembly;
	}

	@Override
	public Collection<Assembly> getAllAssemblies() {
		return em.createNamedQuery(Assembly.ALL_ASSEMBLIES, Assembly.class).getResultList();
	}

	@Override
	public Collection<String> getAllAssemblyIds() {
		return em.createNamedQuery(Assembly.ALL_ASSEMBLY_IDS, String.class).getResultList();
	}

	@Override
	public Bom findBom(BomPK bomPK) throws BomNotFoundException {
		Bom bom = em.find(Bom.class, bomPK);
		if (bom == null) {
			throw new BomNotFoundException("The Bom with the BomPK " + bomPK + " was not found!");
		}
		return bom;
	}

	@Override
	public Collection<Bom> getAllBoms() {
		return em.createNamedQuery(Bom.ALL_BOMS, Bom.class).getResultList();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Inventory getInventory(String componentId, Integer location) throws InventoryNotFoundException {
		Inventory inventory = em.find(Inventory.class, new InventoryPK(componentId, location));
		if (inventory == null) {
			throw new InventoryNotFoundException("The Inventory with the Component/Assembly id " + componentId
					+ " and location " + location + " was not found!");
		}
		return inventory;
	}

	@Override
	public Collection<Inventory> getAllInventories() {
		return em.createNamedQuery(Inventory.ALL_INVENTORIES, Inventory.class).getResultList();
	}

	/**
	 * Inserts delivered components into the Inventory.
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deliver(List<ComponentDemand> componentDemands) throws InventoryNotFoundException {
		for (ComponentDemand componentDemand : componentDemands) {
			//TODO load all inventories for all component demand and location
			Inventory inventory = this.getInventory(componentDemand.getComponentId(), componentDemand.getLocation());
				inventory.addQuantityOnHand(componentDemand.getQuantity());
				// reduce quantity in order
				if (inventory.getQuantityInOrder() - componentDemand.getQuantity() < 0) {
					inventory.reduceQuantityInOrder(inventory.getQuantityInOrder());
				} else {
					inventory.reduceQuantityInOrder(componentDemand.getQuantity());
				}
				inventory.setAccDate(Calendar.getInstance());
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Component createComponent(Component component) {
		em.persist(component);
		return component;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Component deleteComponent(Integer componentId) throws ComponentNotFoundException {
		Component component = this.findComponent(Integer.toString(componentId));
		em.remove(component);
		return component;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Assembly createAssembly(Assembly assembly) {
		em.persist(assembly);
		return assembly;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Inventory createInventory(Inventory inventory) throws ComponentNotFoundException {
		// check if component exists 
		this.findComponent(inventory.getComponentId());
		em.persist(inventory);
		return inventory;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Bom createBom(Bom bom) throws AssemblyNotFoundException, ComponentNotFoundException {
		if (this.findComponent(bom.getComponentId()) != null && this.findAssembly(bom.getAssemblyId()) != null) {
			em.persist(bom);
			return bom;
		} else {
			return null;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void addBomToComponent(BomPK bomPK) throws AssemblyNotFoundException, ComponentNotFoundException, BomNotFoundException {
		Component component = this.findComponent(bomPK.getComponentId());
		Assembly assembly = this.findAssembly(bomPK.getAssemblyId());
		Bom bom = this.findBom(bomPK);
		component.addComponentBoms(Arrays.asList(bom));
		assembly.addComponent(bom);
		return;
	}

}
