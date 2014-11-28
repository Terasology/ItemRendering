#RenderItemComponent

Add this to entities and they will display in the world.  This rendered version's location becomes a child of the owning entity (like the containing inventory). 

#CustomRenderedItemMeshComponent

When the entity gets an RenderItemComponent added, it will use this mesh and material instead of the default block or item.

##Minimal Usage on a prefab

```java
{
    "CustomRenderedItemMesh" : {
        "mesh" : "UriOfYourMesh",
        "material" : "UriOfYourMaterial"
    }
}
```


#AnimateRotationComponent

When the entity gets a RenderItemComponent added, it will rotate continuously.

##Minimal Usage on a prefab

```java
{
    "AnimateRotation" : {
        "yaw|pitch|roll" : "value"
    }
}
```

##Defaults

```java
{
    "AnimateRotation" : {
        "yaw" : "NONE",
        "pitch" : "NONE",
        "roll" : "NONE",
        "speed" : 1,
        "isSynchronized" : "false"
    }
}
```