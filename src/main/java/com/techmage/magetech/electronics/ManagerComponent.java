package com.techmage.magetech.electronics;

import com.techmage.magetech.electronics.component.ComponentPin;
import com.techmage.magetech.electronics.component.ComponentType;
import com.techmage.magetech.electronics.loader.LoaderComponent;
import com.techmage.magetech.item.ItemElectronicComponent;
import com.techmage.magetech.registry.ItemRegistry;
import com.techmage.magetech.utility.LogHelper;
import net.minecraft.item.Item;
import scala.collection.Iterator;
import scala.reflect.io.Directory;
import scala.reflect.io.Path;

import java.io.File;
import java.util.ArrayList;

public class ManagerComponent
{
    private final String MOD_ID;

    private LoaderComponent loaderComponent;

    private ArrayList<ComponentType> loadedComponents;

    public ManagerComponent(String MOD_ID)
    {
        this.MOD_ID = MOD_ID;

        this.loaderComponent = new LoaderComponent(MOD_ID);

        this.loadedComponents = loadAllComponents();

        registerComponents();
    }

    public ArrayList<ComponentType> loadAllComponents()
    {
        LogHelper.info("---------------------------------------------------------------------------------");
        LogHelper.info("                              LOADING COMPONENTS:                                ");
        LogHelper.info("---------------------------------------------------------------------------------");

        ArrayList<ComponentType> componentsLoaded = new ArrayList<>();

        String path = getClass().getClassLoader().getResource("assets/" + MOD_ID.toLowerCase() + "/electronics/component/").getPath();

        Iterator<Path> dir = new Directory(new File(path)).list();

        while (dir.hasNext())
        {
            String dirName = dir.next().name();

            if (dirName.contains(".cmp"))
                componentsLoaded.add(loaderComponent.loadComponent(dirName));
        }

        for (ComponentType componentType : componentsLoaded)
        {
            LogHelper.info("LOAD COMPONENT: " + componentType.getName());
            LogHelper.info("  - PINS:");

            for (ComponentPin pin : componentType.getPins())
                LogHelper.info(String.format("    - %s : %s.%s", pin.getName(), pin.getCordX(), pin.getCordY()));

            LogHelper.info("-----------------------");
        }

        return componentsLoaded;
    }

    public ComponentType getComponentType(String id)
    {
        for (ComponentType componentType : loadedComponents)
        {
            if (id.matches(componentType.getId()))
                return componentType;
        }

        return null;
    }

    private void registerComponents()
    {
        Item COMPONENT;

        for (ComponentType component : loadedComponents)
            COMPONENT = ItemRegistry.registerItem(new ItemElectronicComponent(component.getId()));
    }
}