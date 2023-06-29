module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.cli;
    requires java.rmi;
    requires com.google.gson;
    requires org.fusesource.jansi;

    opens it.polimi.ingsw to javafx.fxml;
    exports it.polimi.ingsw;
    opens it.polimi.ingsw.view to javafx.fxml;
    exports it.polimi.ingsw.view;
    exports it.polimi.ingsw.view.GUIView;
    opens it.polimi.ingsw.view.GUIView to javafx.fxml;



    exports it.polimi.ingsw.controller.client;
    opens it.polimi.ingsw.controller.client;


    exports it.polimi.ingsw.controller;
    opens it.polimi.ingsw.controller;

    exports it.polimi.ingsw.controller.controllerObservers;
    opens it.polimi.ingsw.controller.controllerObservers;

    exports it.polimi.ingsw.controller.pubSub;
    opens it.polimi.ingsw.controller.pubSub;



//SERVER
    exports it.polimi.ingsw.server;
    opens it.polimi.ingsw.server;
//MODEL
    exports it.polimi.ingsw.model;
    opens it.polimi.ingsw.model;
    //MESSAGE
    exports it.polimi.ingsw.model.messageModel;
    opens it.polimi.ingsw.model.messageModel;
    exports it.polimi.ingsw.model.messageModel.errorMessages;
    opens it.polimi.ingsw.model.messageModel.errorMessages;
    exports it.polimi.ingsw.model.messageModel.lobbyMessages;
    opens it.polimi.ingsw.model.messageModel.lobbyMessages;
    exports it.polimi.ingsw.model.messageModel.matchStateMessages;
    opens it.polimi.ingsw.model.messageModel.matchStateMessages;
    exports it.polimi.ingsw.model.messageModel.GameManagerMessages;
    opens it.polimi.ingsw.model.messageModel.GameManagerMessages;
    //HELPERS
    exports it.polimi.ingsw.model.helpers;
    opens it.polimi.ingsw.model.helpers;
    //MODELSUPPORT
    exports it.polimi.ingsw.model.modelSupport;
    opens it.polimi.ingsw.model.modelSupport;
    exports it.polimi.ingsw.model.modelSupport.enums;
    opens it.polimi.ingsw.model.modelSupport.enums;
    exports it.polimi.ingsw.model.modelSupport.exceptions;
    opens it.polimi.ingsw.model.modelSupport.exceptions;
    exports it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions;
    opens it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions;
    //VIRTUAL_MODEL
    exports it.polimi.ingsw.model.virtual_model;
    opens it.polimi.ingsw.model.virtual_model;
    //COMMONGOALS
    exports it.polimi.ingsw.model.CommonGoals;
    opens it.polimi.ingsw.model.CommonGoals;
    exports it.polimi.ingsw.model.CommonGoals.Strategy;
    opens it.polimi.ingsw.model.CommonGoals.Strategy;
//



}