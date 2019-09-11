package de.sopra.javagame.control.ai2;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ai.AIProcessor;
import de.sopra.javagame.control.ai.ClassUtil;
import de.sopra.javagame.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static de.sopra.javagame.control.ai2.DecisionResult.*;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
public class DecisionMaker implements AIProcessor {

    private List<Pair<DoAfter, Class<? extends Decision>>> decisionClasses;

    private HashMap<DecisionResult, Decision> decisionTowers;

    private void findDecisions() {
        try {
            List<Class> classes = ClassUtil.getClasses("de.sopra.javagame.control.ai2.decisions");
            decisionClasses = classes.stream()
                    .filter(Decision.class::isAssignableFrom)
                    .map(clazz -> (Class<? extends Decision>) clazz)
                    .map(clazz -> new Pair<DoAfter, Class<? extends Decision>>(clazz.getDeclaredAnnotation(DoAfter.class), clazz))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildTowers() {
        for (DecisionResult decisionType : DecisionResult.values()) {

            //Alle für diesen Tower relevanten Decisions herausfiltern
            List<Pair<DoAfter, Class<? extends Decision>>> towerDecisions = decisionClasses.stream()
                    .filter(pair -> Collections.singletonList(pair.getLeft()).contains(decisionType))
                    .collect(Collectors.toList());

            //generiere geordnete Queue entsprechend der Abhängigkeiten untereinander
            LinkedList<Class<? extends Decision>> buildingQueue = buildingQueue(towerDecisions);

            //Tower linear aus Queue generieren
            Decision decisionTower = buildTower(buildingQueue);

            //tower der Sammlung hinzufügen
            decisionTowers.put(decisionType, decisionTower);

        }
    }

    private LinkedList<Class<? extends Decision>> buildingQueue
            (List<Pair<DoAfter, Class<? extends Decision>>> towerDecisions) {

        Queue<Pair<DoAfter, Class<? extends Decision>>> buildingQueue = new LinkedList<>();
        List<Pair<DoAfter, Class<? extends Decision>>> dependenciesMissing = new LinkedList<>();

        //Aufteilen von Decisions auf beide Listen, Queue wird nur befüllt, wenn die Abhängigkeit erfüllt ist
        towerDecisions.forEach(pair -> {
            if (buildingQueue.stream().anyMatch(queuedPair -> queuedPair.getRight().equals(pair.getRight())))
                buildingQueue.offer(pair);
            else dependenciesMissing.add(pair);
        });

        //Nachfüllen aller bisher nicht erfüllten Abhängigkeiten im Loop bis keine Abhängigkeit mehr gelöst werden kann
        //das passiert zum Beispiel, wenn Ring-Abhängigkeiten existieren
        boolean done = false;
        while (!done) {
            done = true;
            for (Pair<DoAfter, Class<? extends Decision>> pair : dependenciesMissing) {
                if (buildingQueue.stream().anyMatch(queuedPair -> queuedPair.getRight().equals(pair.getRight()))) {
                    buildingQueue.offer(pair);
                    done = false;
                }
            }
        }

        return buildingQueue.stream()
                .map(Pair::getRight)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Decision buildTower(Queue<Class<? extends Decision>> queuedDecisions) {
        if (!queuedDecisions.isEmpty()) {
            Decision tower = ClassUtil.create(queuedDecisions.poll());
            if (tower != null) {
                while (!queuedDecisions.isEmpty()) {
                    tower = tower.next(ClassUtil.create(queuedDecisions.poll()));
                }
            }
            return tower;
        }
        return null;
    }

    private Decision decide(AIController control, DecisionResult result) {
        Decision decision = decisionTowers.get(result);
        if (decision != null)
            decision.setControl(control);
        return decision;
    }

    private Decision makeTurnDecision(AIController control) {
        return decide(control, TURN_ACTION);
    }

    private Decision makeDiscardDecision(AIController control) {
        return decide(control, DISCARD);
    }

    private Decision makeSpecialCardDecision(AIController control) {
        return decide(control, PLAY_SPECIAL_CARD);
    }

    @Override
    public void init() {
        findDecisions();
        buildTowers();
    }

    @Override
    public void makeStep(AIController control) {
        if (control.isCurrentlyDiscarding()) {
            Decision decision = makeDiscardDecision(control);
            decision.act();
        } else {
            Decision turn = makeTurnDecision(control);
            turn.act();
            Decision special = makeSpecialCardDecision(control);
            if (special != null) //Nicht immer müssen Spezialkarten gespielt werden
                special.act();
        }
    }

    @Override
    public String getTip(AIController control) {
        return null; //TODO
    }

}