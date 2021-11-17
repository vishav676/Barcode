package com.vishav.barcode;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.ViewModels.TicketTableVM;
import com.vishav.barcode.databinding.FragmentFileBinding;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class FileFragment extends Fragment {

    private FragmentFileBinding binding;
    private TicketTableVM ticketTableVm;
    private String TAG = "Message";
    private EditText listName;
    String[]  permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    private static final int FILE_SELECT_CODE = 676;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFileBinding.inflate(inflater,container,false);
        listName = getActivity().findViewById(R.id.newListName);
        ticketTableVm = new ViewModelProvider(this).get(TicketTableVM.class);
        binding.fileChooser.setOnClickListener(v -> {

            if (TextUtils.isEmpty(listName.getText())){
                Toast.makeText(getContext(), "Please Provide List Name", Toast.LENGTH_SHORT).show();
            }
            else{
                showFileChooser();
            }

        });
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(), permissions, 1);
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, 1);
            }
        }
        return binding.getRoot();
    }

    private void showFileChooser(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent = Intent.createChooser(intent,"Choose a file");

        try {
            startActivityForResult(Intent.createChooser(intent, "Open CSV"),
                    FILE_SELECT_CODE);
        }
        catch (ActivityNotFoundException ex)
        {
            Toast.makeText(getContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == FILE_SELECT_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                readCSV(data.getData());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void readCSV(Uri file)
    {
        Log.d(TAG,"Read Excel File");
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            String newListName = listName.getText().toString();
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            TicketListTable newTicketListTable = new TicketListTable();
            newTicketListTable.setTicketListName(newListName);
            newTicketListTable.setTicketListCreated(sd.format(Calendar.getInstance().getTime()));
            newTicketListTable.setTicketListUpdated(sd.format(Calendar.getInstance().getTime()));

            long ticketTableListId = 0;
            try {
                ticketTableListId = ticketTableVm.insert(newTicketListTable);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(ticketTableListId<=0){
                throw new ArithmeticException();
            }

            FormulaEvaluator formulaCovertor = workbook.getCreationHelper().createFormulaEvaluator();
            for (int position = 1; position < rowsCount; position++)
            {
                Row row = sheet.getRow(position);
                String number = getCellAsString(row,0,formulaCovertor);
                String info = getCellAsString(row,1,formulaCovertor);
                String warningNote = getCellAsString(row,2,formulaCovertor);
                String warning = getCellAsString(row,3,formulaCovertor);
                String customer = getCellAsString(row,4,formulaCovertor);
                int useable = (int)(Float.parseFloat(getCellAsString(row,5,formulaCovertor)));
                TicketTable ticketTable = new TicketTable(number, customer, info, warningNote, useable, ticketTableListId,warning);
                ticketTableVm.insert(ticketTable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCellAsString(Row position, int column, FormulaEvaluator formulaCovertor) {
        String value = "";
        try {
            Cell cell = position.getCell(column);
            CellValue cellValue = formulaCovertor.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage() );
        }
        return value;
    }
}