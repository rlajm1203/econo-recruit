package com.econovation.recruitdomain.domains.applicant.domain;

public class ApplicantState {

    private PassStates state;

    public ApplicantState(){
        this.state = PassStates.NONPASSED; // 초기 상태
    }

    public void nextState(){
        this.state = this.state.next();
    }

    public void prevState(){
        this.state = this.state.prev();
    }

}
