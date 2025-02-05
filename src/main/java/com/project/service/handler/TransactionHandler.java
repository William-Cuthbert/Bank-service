package com.project.service.handler;

import com.project.service.logic.TransactionLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Scope("prototype")
@Component("transactionHandler")
public class TransactionHandler {

  private TransactionLogic logic;
  private String sourceId;
  private String targetId;
  private double amount;
  private String reference;

  public TransactionHandler(TransactionLogic logic,
      @Value("#{${sourceId}}") String sourceId, @Value("#{${targetId}}") String targetId,
      @Value("#{${amount}}") double amount, @Value("#{${reference}}") String reference) {
    this.logic = logic;
    this.sourceId = sourceId;
    this.targetId = targetId;
    this.amount = amount;
    this.reference = reference;
  }

  public void execute() {
    log.info("Executing transaction");
    logic.executeLogic(sourceId, targetId, amount, reference);
  }
}
