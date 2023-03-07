# EditableBufferedReader
- [EditableBufferedReader](#editablebufferedreader)
- [Enunciat](#enunciat)
  - [EditableBufferedReader.java](#editablebufferedreaderjava)
  - [Line.java](#linejava)
  - [Test](#test)
- [Model-View-Controller](#model-view-controller)
    - [Patró observer/observable](#patró-observerobservable)

# Enunciat
## EditableBufferedReader.java
Programeu la classe `EditableBufferedReader` que estengui `BufferedReader` amb els mètodes:

* **`setRaw:`** Passa la consola de mode cooked a mode raw.

* **`unsetRaw:`** Passa la consola de mode raw a mode cooked.

* **`read:`** Llegeix el següent caràcter o la següent tecla de cursor.

* **`readLine:`** Llegeix la línia amb possibilitat d’editar-la.

Fixeu-vos que `read` llegeix caràcters simples o símbols— les tecles d’edició— com a sencers. Caldrà que els sencers retornats com a símbols no es solapin amb els sencers retornats com a caràcters simples.

El reconeixement de símbols s’ha de fer de dues formes: la primera versió utilitzant tècniques de ***parsing*** semblants a les vistes a classe i després fent servir la classe **`Scanner`** i **expressions regulars**. S’ha de comparar la complexitat de les dues implementacions i treure’n conclusions.

La classe `Scanner` te les seves subtileses. En particular no esta massa pensada per fer-la servir com a parser general. Una discussió dels seus problemes es troba a [Scanner_bugs](http://forums.oracle.com/forums/thread.jspa?threadID=2133193). Trobeu una implementació que solventi aquestes subtileses.

## Line.java
Encapsula 3 atributs:
* Seqüència de caràcters
* Posició del cursor
* Mode d'edició (insert/append)

S'utilitzarà dins el mètode readLine

## Test
Es dona feta




# Model-View-Controller

Interessa abstreure la presentació (vista) del model de dades (estat). Idealment canvis en el odel comporten automàticament actualització de la presentació.
* **Model:** L'estat de l'aplicació, que canvia.
* **View:** Presentció de l'aplicació. Un model pot tenir més d'una.
* **Controller:** Gestor d'esdeveniments d'input. Invoca canvis d'estat del model.

```diagram
Controller->Model: usa/modifica
Model -> View: ús indirecte
View -> Model: demana estat a (observer/observable)
View->Controller: event-driven user-gesture
Controller -> -View: modifica vista (fa visible una part).
```

Hi ha dos implementacions:
* **Model push:** S'envia notificació i canvis.
* **Model pull:** S'envia notificació sense canvis.

Implementat per primer cop el 1979 a Smalltalk-80 (gran influència en el disseny de GUIs posteriors).

```sequence

```

MVC desacopla els tres elements i poden canviar la vista. El model notifica a la vista dels seus canvis, actualitzant la seva presentació. El model no ha de conèixer els detalls de la vista o vistes.

### Patró observer/observable
Els observers (vistes) es regstren amb l'Observable (model). El cmodel informa les vistes amb notifyObserver quan el seu estat canvia. Internament manté una llista d'observers registrats. notifyObservers invoca update de cada vista per actualitzar-la.

```java
class Observable{
    void addObserver(Observer o);
    void notifyObservers(); // Pull
    void notifyObservers(); // Push
    void setChanged();
}

interface Observer{
    void update(Observable o, Object arg);
}
```