package pl.android.puzzledepartment.managers;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.objects.EntityModel;
import pl.android.puzzledepartment.util.OBJLoader;

/**
 * Created by Maciek Ruszczyk on 2017-10-20.
 */

public class EntityManager {
    private final Context context;
    private Map<Integer, EntityModel> entitiesModels;

    public EntityManager(Context context) {
        this.context = context;
        entitiesModels = new HashMap<Integer, EntityModel>();
    }

    public EntityModel getEntityModel(int resourceId) {
        EntityModel entityModel = entitiesModels.get(resourceId);
        if(entityModel != null)
            return entityModel;
        else {
            entityModel = OBJLoader.loadObjModel(context, resourceId);
            entitiesModels.put(resourceId, entityModel);
            return entityModel;
        }
    }

    public EntityModel getDepartmentElka(){
        return getEntityModel(R.raw.elka);
    }

    public EntityModel getDepartmentMel(){
        return getEntityModel(R.raw.mel);
    }

    public EntityModel getDepartmentMech(){
        return getEntityModel(R.raw.mech);
    }

    public EntityModel getDepartmentMini(){
        return getEntityModel(R.raw.mini);
    }
}
