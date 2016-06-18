package yeti.client;

import javafx.util.Pair;
import yeti.algo.results.ResultData;
import yeti.messages.PositionAnswer;
import yeti.messages.Result;

import java.util.ArrayList;
import java.util.List;

public class Integrator extends Thread {
    private class Status {
        private int servers;
        private final short id;
        private List<Pair<PositionAnswer, String>> positionAnswers;

        private Status(int servers, short id) {
            this.servers = servers;
            this.id = id;
            this.positionAnswers = new ArrayList<>();
        }

        private void addPositionAnswer(PositionAnswer positionAnswer, String ip) {
            positionAnswers.add(new Pair<>(positionAnswer, ip));
            servers--;
            if (servers == 0) {
                print();
            }
        }

        private void print() {
            int remaining = 0;
            String result = "";
            for (Pair<PositionAnswer, String> positionAnswerIpPair : positionAnswers) {
                remaining += positionAnswerIpPair.getKey().getLength();
                String position;
                if (positionAnswerIpPair.getKey().getPosition() == 0) {
                    position = "NOT QUEUED";
                } else if (positionAnswerIpPair.getKey().getPosition() == 1) {
                    position = "RUNNING";
                } else {
                    position = "QUEUED(" + (positionAnswerIpPair.getKey().getPosition() - 1) + ")";
                }
                result += positionAnswerIpPair.getValue() + " : " + position + "\n";
            }
            System.out.println();
            System.out.println("REMAINING: " + remaining);
            System.out.println(result);
        }
    }

    private class Finish {
        private ResultData resultData;
        private final short id;
        private int parts;

        private Finish(short id, int parts, ResultData resultData) {
            this.resultData = resultData;
            this.parts = parts;
            this.id = id;
        }

        private void addResult(Result result) {
            parts--;
            resultData = resultData.merge(result.getResultData());
            if (parts == 0) {
                print();
            }
        }

        private void print() {
            System.out.println("RESULT OF " + id + " IS " + resultData.resultToString());
        }
    }

    private List<Status> statusList;
    private List<Finish> finishList;

    public Integrator() {
        statusList = new ArrayList<>();
        finishList = new ArrayList<>();
    }

    public void setupStatus(int servers, short id) {
        statusList.add(new Status(servers, id));
    }

    public void setupFinish(int packets, short id, ResultData emptyResult) {
        finishList.add(new Finish(id, packets, emptyResult));
    }

    public void addStatusResponse(PositionAnswer positionAnswer, String ip) {
        for (Status status : statusList) {
            if (status.id == positionAnswer.getId()) {
                status.addPositionAnswer(positionAnswer, ip);
                break;
            }
        }
    }

    public void addResultResponse(Result result) {
        for (Finish f : finishList) {
            if (f.id == result.getId()) {
                f.addResult(result);
                break;
            }
        }
    }
}

