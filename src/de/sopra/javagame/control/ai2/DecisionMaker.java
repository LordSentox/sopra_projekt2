package de.sopra.javagame.control.ai2;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ai.AIProcessor;
import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.ClassUtil;
import de.sopra.javagame.control.ai2.decisions.Decision;
import de.sopra.javagame.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
                    .filter(clazz -> clazz.isAnnotationPresent(DoAfter.class))
                    .map(clazz -> (Class<? extends Decision>) clazz)
                    .map(clazz -> new Pair<DoAfter, Class<? extends Decision>>(clazz.getDeclaredAnnotation(DoAfter.class), clazz))
                    .collect(Collectors.toList());
//            for (Pair<DoAfter, Class<? extends Decision>> clazz : decisionClasses)
//                System.out.println("> " + clazz.getRight().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildTowers() {
        decisionTowers = new HashMap<>();
        for (DecisionResult decisionType : DecisionResult.values()) {

            //Alle für diesen Tower relevanten Decisions herausfiltern
            List<Pair<DoAfter, Class<? extends Decision>>> towerDecisions = decisionClasses.stream()
                    .filter(pair -> pair.getLeft().act().equals(decisionType))
                    .collect(Collectors.toList());

            //generiere geordnete Queue entsprechend der Abhängigkeiten untereinander
            LinkedList<Class<? extends Decision>> buildingQueue = buildingQueue(towerDecisions);

            //Tower linear aus Queue generieren
            Decision decisionTower = buildTower(buildingQueue);

            //tower der Sammlung hinzufügen
            decisionTowers.put(decisionType, decisionTower);

        }
    }

    List<Pair<DoAfter, Class<? extends Decision>>> getDecisionClasses() {
        return decisionClasses;
    }

    LinkedList<Class<? extends Decision>> buildingQueue
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
            Class<? extends Decision> poll = queuedDecisions.poll();
            Decision tower = ClassUtil.create(poll);
            tower.setPreCondition(poll.getDeclaredAnnotation(PreCondition.class));
            if (tower != null) {
                while (!queuedDecisions.isEmpty()) {
                    Class<? extends Decision> polled = queuedDecisions.poll();
                    Decision lessImportantDecision = ClassUtil.create(polled);
                    lessImportantDecision.setPreCondition(polled.getDeclaredAnnotation(PreCondition.class));
                    tower = tower.next(lessImportantDecision);
                }
            }
            return tower;
        }
        return null;
    }

    private Decision decide(AIController control, DecisionResult result) {
        Decision decision = decisionTowers.get(result);
        if (decision != null) {
            decision.setControl(control);
            return decision.decide();
        } else return Decision.empty();
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

    private Decision makeSafetyDecision(AIController control) {
        return decide(control, SWIM_TO_SAFETY);
    }

    @Override
    public void init() {
        findDecisions();
        buildTowers();
    }

    @Override
    public void makeStep(AIController control) {
        ActionQueue tip = getTip(control); //makeStep soll eigentlich nur den Tip in die Tat umsetzen
        control.doSteps(tip);
    }

    @Override
    public ActionQueue getTip(AIController control) {
        ActionQueue actionQueue;
        if (control.isCurrentlyDiscarding()) { //entweder ist der Spieler mit abwerfen beschäftigt
            Decision decision = makeDiscardDecision(control);
            actionQueue = decision.act();
        } else if (control.isCurrentlyRescueingHimself()) { //oder damit sich selbst zu retten
            Decision decision = makeSafetyDecision(control);
            actionQueue = decision.act();
        } else { //ansonsten ist er einfach am Zug
            Decision turn = makeTurnDecision(control);
            actionQueue = turn.act();
        }
        //für den eigentlich nicht wahrscheinlichen Fall, dass die Queue null ist, eine Sicherheitsmaßnahme
        if (actionQueue == null)
            actionQueue = new ActionQueue(control.getActivePlayer().getType());
        Decision special = makeSpecialCardDecision(control);
        if (special != null) //Nicht immer müssen Spezialkarten gespielt werden
            actionQueue.nextActions(special.act());
        return actionQueue;
    }

}