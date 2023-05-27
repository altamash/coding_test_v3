package com.smallworld.data;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class Transaction {
    // Represent your transaction data here.
    private Long mtn; // 663458,
    private Double amount; // 430.2,
    private String senderFullName; // "Tom Shelby",
    private int senderAge; // 22,
    private String beneficiaryFullName; // "Alfie Solomons",
    private int beneficiaryAge; // 33,
    private Integer issueId; // 1,
    private boolean issueSolved; // false,
    private String issueMessage; // "Looks like money laundering"

    @Override
    public String toString() {
        return "Transaction{" +
                "mtn=" + mtn +
                ", amount=" + amount +
                ", senderFullName='" + senderFullName + '\'' +
                ", senderAge=" + senderAge +
                ", beneficiaryFullName='" + beneficiaryFullName + '\'' +
                ", beneficiaryAge=" + beneficiaryAge +
                ", issueId=" + issueId +
                ", issueSolved=" + issueSolved +
                ", issueMessage='" + issueMessage + '\'' +
                '}';
    }
}
