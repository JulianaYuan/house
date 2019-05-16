package com.juliana.house.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.juliana.house.R;
import com.juliana.house.db.HousesDB;
import com.juliana.house.model.House;

import java.util.ArrayList;
import java.util.List;

public class TaxLoanActivity extends HouseBaseActivity implements View.OnClickListener {
    Button backHouse;
    Button refreshLoan;

    RadioGroup loanSelectRatio;

    EditText commercialEdit;
    EditText providentFundEdit;

    EditText commercialRateEdit;
    EditText providentFundRateEdit;

    EditText totalLoanEdit;
    EditText totalInterestEdit;

    Spinner stagesSpin;
    ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<String>();
    private List<Double> loanList = new ArrayList<Double>();

    private House m_house = null;
    private HousesDB m_housesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tax_loan);

        backHouse = (Button) findViewById(R.id.back_house);
        refreshLoan = (Button) findViewById(R.id.refresh_loan);
        loanSelectRatio = (RadioGroup)findViewById(R.id.loan_select_ratio);
        loanSelectRatio.check(R.id.eq_principal_rb);

        commercialEdit = (EditText)findViewById(R.id.commercial_edit);
        commercialEdit.setEnabled(false);
        providentFundEdit = (EditText)findViewById(R.id.provident_fund_edit);
        providentFundEdit.setEnabled(false);

        commercialRateEdit = (EditText)findViewById(R.id.commercial_rate_edit);
        commercialRateEdit.setText("4.90%");
        providentFundRateEdit = (EditText)findViewById(R.id.provident_fund_rate_edit);
        providentFundRateEdit.setText("3.25%");

        totalLoanEdit = (EditText)findViewById(R.id.total_loan_edit);
        totalLoanEdit.setEnabled(false);
        totalInterestEdit = (EditText)findViewById(R.id.total_interest_edit);
        totalInterestEdit.setEnabled(false);

        stagesSpin = (Spinner)findViewById(R.id.stages_spin);
        stagesSpin.setSelection(19);

        m_housesDB = getHousesDB();
        backHouse.setOnClickListener(this);
        refreshLoan.setOnClickListener(this);
        loanSelectRatio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (m_house!=null)updataHouseState(m_house);
            }
        });

        stagesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (m_house!=null)updataHouseState(m_house);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView = (ListView)findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        int houseId = getIntent().getIntExtra("house_id",0);
        if (houseId!=0)
        {
            Log.i("mainActivity","HouseDetailActivity onCreate houseCode is not empty ");
            m_house = m_housesDB.readHouse(houseId);
            updataHouseState(m_house);
        }
        else{
            Log.i("mainActivity","TaxLoanActivity onCreate houseId is 0,impossible!!! ");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_house:
                /*Intent intent = new Intent(TaxLoanActivity.this,HouseDetailActivity.class);
                Log.i("mainActivity","TaxLoanActivity back_house");
                startActivity(intent);*/
                finish();
                break;
            case R.id.refresh_loan:
                updataHouseState(m_house);
                break;
        }
    }

    private void updataHouseState(House house){
        Double totalLoan = (house.calTaxPrice()-house.calDownPayment())*10000;
        Double totalInterest = 0.0;
        Double commercialLoan = (totalLoan-100*10000)>0?(totalLoan-100*10000):0;
        Double providentFundLoan = (totalLoan-100*10000)>0?100*10000:totalLoan*10000;
        Double commercialYearRate = Double.valueOf(commercialRateEdit.getText().toString().split("%")[0])/100;
        Double commercialMonthRate = commercialYearRate/12;
        Double providentYearRate = Double.valueOf(providentFundRateEdit.getText().toString().split("%")[0])/100;
        Double providentMonthRate = providentYearRate/12;
        int months = (stagesSpin.getSelectedItemPosition()+1)*12;
        Double eqPrincipalInterestMonth = 0.0;

        if (loanSelectRatio.getCheckedRadioButtonId()==R.id.eq_principal_interest_rb){//等额本息
            Double commercialMonth = 0.0;
            Double providentMonth = 0.0;
            Double commercialMonthRatePower = (1+commercialMonthRate);
            Double providentMonthRatePower = (1+providentMonthRate);
            for (int i = 0;i<months;i++)
            {
                commercialMonthRatePower = commercialMonthRatePower*(1+commercialMonthRate);
                providentMonthRatePower = providentMonthRatePower*(1+providentMonthRate);
            }
            commercialMonth = (commercialLoan*commercialMonthRate*commercialMonthRatePower)/(commercialMonthRatePower-1);
            providentMonth = (providentFundLoan*providentMonthRate*providentMonthRatePower)/(providentMonthRatePower-1);
            eqPrincipalInterestMonth = commercialMonth +providentMonth;
            dataList.clear();
            dataList.add("每期应还： " + eqPrincipalInterestMonth.toString() + "元");
            totalInterest = eqPrincipalInterestMonth*months-totalLoan;
        }
        else{//等额本金
            Double totalPrincipalInterest = 0.0;
            dataList.clear();
            for (int i = 0;i<months;i++) {
                Double commercialMonth = 0.0;
                Double providentMonth = 0.0;
                Double eqPrincipalMonth = 0.0;
                commercialMonth = commercialLoan / months + (commercialLoan -(commercialLoan / months)*i)*commercialMonthRate;
                providentMonth = providentFundLoan / months + (providentFundLoan -(providentFundLoan / months)*i)*providentMonthRate;
                eqPrincipalMonth = commercialMonth+providentMonth;
                totalPrincipalInterest = totalPrincipalInterest+eqPrincipalMonth;
                dataList.add("第 "+Integer.toString(i+1)+" 期应还： "+ eqPrincipalMonth.toString() + "元");
            }
            totalInterest = totalPrincipalInterest - totalLoan;
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        commercialEdit.setText(commercialLoan.toString());
        providentFundEdit.setText(providentFundLoan.toString());
        totalLoanEdit.setText(totalLoan.toString());
        totalInterestEdit.setText(totalInterest.toString());
    }
}
