// Decompiled by DJ v3.12.12.101 Copyright 2016 Atanas Neshkov  Date: 3/9/2016 6:05:52 PM
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SortingSleuth.java

import edu.neu.ccs.Stringable;
import edu.neu.ccs.filter.*;
import edu.neu.ccs.gui.*;
import edu.neu.ccs.util.JPTConstants;
import java.awt.*;
import java.io.PrintStream;
import javax.swing.Action;

public class SortingSleuth extends DisplayPanel
    implements JPTConstants
{
    private class StringWrapper
        implements Stringable
    {

        public void fromStringData(String data)
        {
            value = data;
        }

        public String toStringData()
        {
            return value;
        }

        private String value;
        final SortingSleuth this$0;

        private StringWrapper()
        {
            this$0 = SortingSleuth.this;
            super();
        }
    }

    private class StudentIdFilter
        implements StringableFilter
    {

        public Stringable filterStringable(Stringable wrapper)
            throws FilterException
        {
            String data = wrapper.toStringData();
            if(data.length() != 7)
                throw new FilterException(wrapper, "Please enter a student ID with 7 digits.");
            try
            {
                Integer.parseInt(data);
            }
            catch(NumberFormatException exc)
            {
                throw new FilterException(wrapper, exc.toString());
            }
            return wrapper;
        }

        final SortingSleuth this$0;

        private StudentIdFilter()
        {
            this$0 = SortingSleuth.this;
            super();
        }

        StudentIdFilter(StudentIdFilter studentidfilter)
        {
            this();
        }
    }


    public static void main(String args[])
    {
        JPTFrame.createQuickJPTFrame("Sorting Sleuth", new SortingSleuth());
    }

    public SortingSleuth()
    {
        studentIdTFV = new TextFieldView("0000000", "ID must be a 7 digit number:", "Unrecognized student ID.");
        studentIdFilter = new StudentIdFilter(null);
        studentIdSectionDisplay = new Display(studentIdTFV, null, "Student ID");
        orderOptionsView = new OptionsView(Orderings, 3, new GridLayout(2, 2));
        listSizeSlider = new SliderView(0, 401, 2000, 500);
        sizeTFV = new TextFieldView("32", "Size must be between 16 and 1048576:", "Incorrect input");
        sizeRangeFilter = new edu.neu.ccs.filter.RangeFilter.Long(16L, true, 0x100000L, true);
        createList = new SimpleAction("Create The List") {

            public void perform()
            {
                makeNewList();
            }

            final SortingSleuth this$0;

            
            {
                this$0 = SortingSleuth.this;
                super($anonymous0);
            }
        }
;
        listActions = new ActionsPanel(new Action[] {
            createList
        }, new GridLayout(1, 0));
        sizes = new DisplayCollection(new Displayable[] {
            listSizeSlider, sizeTFV
        }, 0);
        listSection = new DisplayCollection(new Displayable[] {
            orderOptionsView, sizes, listActions
        }, 1);
        listSectionDisplay = new Display(listSection, null, "List Properties");
        studentId = "0000000";
        selectedPermutation = 0;
        listSizeTFV = new TextFieldView("");
        listTypeTFV = new TextFieldView("");
        buttonTFV = new TextFieldView("");
        comparisonsTFV = new TextFieldView("");
        movementsTFV = new TextFieldView("");
        timeTFV = new TextFieldView("");
        outputList = (new Displayable[] {
            new DisplayWrapper(new Display(listSizeTFV, "                      N:", null)), new DisplayWrapper(new Display(listTypeTFV, "       DataType:", null)), new DisplayWrapper(new Display(buttonTFV, "                 Sort:", null)), new DisplayWrapper(new Display(comparisonsTFV, "Comparisons:", null)), new DisplayWrapper(new Display(movementsTFV, "    Movements:", null)), new DisplayWrapper(new Display(timeTFV, "       Total time:", null))
        });
        dataFields = new DisplayCollection(outputList);
        dataFieldsDisplay = new Display(dataFields, null, "Experimental Results");
        rightSide = new DisplayCollection(new Displayable[] {
            studentIdSectionDisplay, listSectionDisplay, dataFieldsDisplay
        }, 1);
        sorts = (new Action[] {
            new SimpleAction(buttonNames[0]) {

                public void perform()
                {
                    runExperiment(0);
                }

                final SortingSleuth this$0;

            
            {
                this$0 = SortingSleuth.this;
                super($anonymous0);
            }
            }
, new SimpleAction(buttonNames[1]) {

                public void perform()
                {
                    runExperiment(1);
                }

                final SortingSleuth this$0;

            
            {
                this$0 = SortingSleuth.this;
                super($anonymous0);
            }
            }
, new SimpleAction(buttonNames[2]) {

                public void perform()
                {
                    runExperiment(2);
                }

                final SortingSleuth this$0;

            
            {
                this$0 = SortingSleuth.this;
                super($anonymous0);
            }
            }
, new SimpleAction(buttonNames[3]) {

                public void perform()
                {
                    runExperiment(3);
                }

                final SortingSleuth this$0;

            
            {
                this$0 = SortingSleuth.this;
                super($anonymous0);
            }
            }
, new SimpleAction(buttonNames[4]) {

                public void perform()
                {
                    runExperiment(4);
                }

                final SortingSleuth this$0;

            
            {
                this$0 = SortingSleuth.this;
                super($anonymous0);
            }
            }
, new SimpleAction(buttonNames[5]) {

                public void perform()
                {
                    runExperiment(5);
                }

                final SortingSleuth this$0;

            
            {
                this$0 = SortingSleuth.this;
                super($anonymous0);
            }
            }

        });
        sortActions = new ActionsPanel(sorts, new GridLayout(sorts.length, 0));
        sortsDisplay = new Display(sortActions, null, "Which Sort Is Which?");
        setLayout(new BorderLayout());
        add(rightSide, "East");
        add(sortsDisplay, "West");
        sortsDisplay.setPreferredSize(new Dimension(200, 200));
        sizeTFV.setPreferredWidth(60);
        listSizeTFV.setPreferredWidth(150);
        listTypeTFV.setPreferredWidth(150);
        buttonTFV.setPreferredWidth(150);
        comparisonsTFV.setPreferredWidth(150);
        movementsTFV.setPreferredWidth(150);
        timeTFV.setPreferredWidth(150);
        studentIdTFV.addActionListener(new SimpleAction() {

            public void perform()
            {
                setStudentId();
            }

            final SortingSleuth this$0;

            
            {
                this$0 = SortingSleuth.this;
                super();
            }
        }
);
        listSizeSlider.addSlidingAction(new SimpleAction() {

            public void perform()
            {
                setListWindowFromSlider();
            }

            final SortingSleuth this$0;

            
            {
                this$0 = SortingSleuth.this;
                super();
            }
        }
);
        sizeTFV.addActionListener(new SimpleAction() {

            public void perform()
            {
                setSliderFromListWindow();
            }

            final SortingSleuth this$0;

            
            {
                this$0 = SortingSleuth.this;
                super();
            }
        }
);
        makeNewList();
    }

    public void runExperiment(int buttonId)
    {
        setStudentId();
        String sortName = buttonNames[buttonId];
        SortingExperiment se = experimentOrders[selectedPermutation][buttonId];
        int listCopy[] = new int[theList.length];
        for(int i = 0; i < theList.length; i++)
            listCopy[i] = theList[i];

        se.performExperiment(listCopy);
        if(!isSorted(listCopy))
        {
            System.out.println("Sort FAILED");
            if(listCopy.length < 33)
            {
                for(int i = 0; i < listCopy.length; i++)
                    System.out.print((new StringBuilder(" ")).append(listCopy[i]).toString());

                System.out.println("");
            }
        }
        buttonTFV.setViewState(sortName);
        comparisonsTFV.setViewState((new StringBuilder()).append(se.getComparisons()).toString());
        movementsTFV.setViewState((new StringBuilder()).append(se.getMovements()).toString());
        timeTFV.setViewState((new StringBuilder()).append(se.getTime()).toString());
    }

    private boolean isSorted(int list[])
    {
        for(int i = 0; i < list.length - 1; i++)
            if(list[i] > list[i + 1])
            {
                System.out.println((new StringBuilder("List failed to be sorted at position ")).append(i).toString());
                return false;
            }

        return true;
    }

    public void makeNewList()
    {
        setSliderFromListWindow();
        int size = sizeTFV.demandInt(sizeRangeFilter);
        theList = new int[size];
        listType = orderOptionsView.getSelectedIndex();
        switch(listType)
        {
        default:
            break;

        case 0: // '\0'
            for(int i = 0; i < theList.length; i++)
                theList[i] = i;

            break;

        case 1: // '\001'
            for(int i = 0; i < theList.length; i++)
                theList[i] = theList.length - 1 - i;

            break;

        case 2: // '\002'
            for(int i = 0; i < theList.length; i++)
                theList[i] = i;

            for(int j = 0; (double)j < (double)theList.length * 0.050000000000000003D; j++)
            {
                int randomIndex1 = (int)(Math.random() * (double)theList.length);
                int randomIndex2 = (int)(Math.random() * (double)theList.length);
                int temp = theList[randomIndex1];
                theList[randomIndex1] = theList[randomIndex2];
                theList[randomIndex2] = temp;
            }

            break;

        case 3: // '\003'
            for(int i = 0; i < theList.length; i++)
                theList[i] = i;

            for(int j = theList.length - 1; j > 0; j--)
            {
                int randomIndex = (int)(Math.random() * (double)j);
                int temp = theList[j];
                theList[j] = theList[randomIndex];
                theList[randomIndex] = temp;
            }

            break;
        }
        listSizeTFV.setViewState((new StringBuilder()).append(theList.length).toString());
        listTypeTFV.setViewState(Orderings[listType]);
        comparisonsTFV.setViewState("");
        movementsTFV.setViewState("");
        timeTFV.setViewState("");
        if(theList.length < 33)
        {
            for(int i = 0; i < theList.length; i++)
                System.out.print((new StringBuilder(" ")).append(theList[i]).toString());

            System.out.println("");
        }
    }

    public void setStudentId()
    {
        studentId = studentIdTFV.demandString(studentIdFilter);
        selectedPermutation = Integer.parseInt(studentId) % 7;
    }

    public void setListWindowFromSlider()
    {
        double sliderValue = (double)listSizeSlider.getValue() / 100D;
        int listSize = (int)Math.exp(sliderValue * Math.log(2D));
        sizeTFV.setViewState((new StringBuilder()).append(listSize).toString());
    }

    public void setSliderFromListWindow()
    {
        int listSize = sizeTFV.demandInt(sizeRangeFilter);
        int sliderValue = (int)((100D * Math.log(listSize)) / Math.log(2D));
        listSizeSlider.setValue(sliderValue);
    }

    private static final String DEFAULT_STUDENT_ID = "0000000";
    private static final int STUDENT_ID_LENGTH = 7;
    private static final int LOG_MIN_SIZE100 = 401;
    private static final int LOG_MAX_SIZE100 = 2000;
    private static final int LOG_DEFAULT_SIZE100 = 500;
    private static final String DEFAULT_SIZE = "32";
    private static final int MIN_SIZE = 16;
    private static final int MAX_SIZE = 0x100000;
    private static final int IN_ORDER = 0;
    private static final int REVERSE_ORDER = 1;
    private static final int ALMOST_ORDER = 2;
    private static final int RANDOM_ORDER = 3;
    private static final int DEFAULT_ORDER = 3;
    private static final double DISORDER_FRACTION = 0.050000000000000003D;
    public static final String Orderings[] = {
        "InOrder", "ReverseOrder", "AlmostOrder", "Random"
    };
    private TextFieldView studentIdTFV;
    private StudentIdFilter studentIdFilter;
    private Display studentIdSectionDisplay;
    private OptionsView orderOptionsView;
    private SliderView listSizeSlider;
    private TextFieldView sizeTFV;
    private edu.neu.ccs.filter.RangeFilter.Long sizeRangeFilter;
    private SimpleAction createList;
    private ActionsPanel listActions;
    private DisplayCollection sizes;
    private DisplayCollection listSection;
    private Display listSectionDisplay;
    int theList[];
    int listType;
    String studentId;
    int selectedPermutation;
    private TextFieldView listSizeTFV;
    private TextFieldView listTypeTFV;
    private TextFieldView buttonTFV;
    private TextFieldView comparisonsTFV;
    private TextFieldView movementsTFV;
    private TextFieldView timeTFV;
    private Displayable outputList[];
    private DisplayCollection dataFields;
    private Display dataFieldsDisplay;
    private DisplayCollection rightSide;
    private static final String buttonNames[] = {
        "Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta"
    };
    private static final SortingExperiment experimentOrders[][];
    private Action sorts[];
    private ActionsPanel sortActions;
    private Display sortsDisplay;

    static 
    {
        experimentOrders = (new SortingExperiment[][] {
            new SortingExperiment[] {
                SortingExperiment.mergeSort, SortingExperiment.heapSort, SortingExperiment.bubbleSort, SortingExperiment.quickSort, SortingExperiment.insertionSort, SortingExperiment.selectionSort
            }, new SortingExperiment[] {
                SortingExperiment.selectionSort, SortingExperiment.bubbleSort, SortingExperiment.heapSort, SortingExperiment.mergeSort, SortingExperiment.quickSort, SortingExperiment.insertionSort
            }, new SortingExperiment[] {
                SortingExperiment.quickSort, SortingExperiment.selectionSort, SortingExperiment.insertionSort, SortingExperiment.bubbleSort, SortingExperiment.mergeSort, SortingExperiment.heapSort
            }, new SortingExperiment[] {
                SortingExperiment.bubbleSort, SortingExperiment.insertionSort, SortingExperiment.quickSort, SortingExperiment.heapSort, SortingExperiment.selectionSort, SortingExperiment.mergeSort
            }, new SortingExperiment[] {
                SortingExperiment.heapSort, SortingExperiment.bubbleSort, SortingExperiment.insertionSort, SortingExperiment.mergeSort, SortingExperiment.selectionSort, SortingExperiment.quickSort
            }, new SortingExperiment[] {
                SortingExperiment.insertionSort, SortingExperiment.heapSort, SortingExperiment.mergeSort, SortingExperiment.quickSort, SortingExperiment.bubbleSort, SortingExperiment.selectionSort
            }, new SortingExperiment[] {
                SortingExperiment.mergeSort, SortingExperiment.quickSort, SortingExperiment.selectionSort, SortingExperiment.insertionSort, SortingExperiment.heapSort, SortingExperiment.bubbleSort
            }
        });
    }
}
