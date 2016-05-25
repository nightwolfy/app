package it.wolfy.app.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Persona extends RealmObject
{
    public int id;
    public String nome;
    public String dataNascita;
    public RealmList<Misurazione> misurazioni;

    public Persona()
    {
        misurazioni = new RealmList<>();
    }
}
