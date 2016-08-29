// Decompiled by DJ v3.12.12.101 Copyright 2016 Atanas Neshkov  Date: 3/9/2016 6:07:23 PM
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SortingExperiment.java

import java.util.Date;

public class SortingExperiment
{

    public SortingExperiment()
    {
    }

    public void performExperiment(int list[])
    {
        clearStats();
        Date start = new Date();
        sort(list);
        Date end = new Date();
        time = end.getTime() - start.getTime();
    }

    public int getComparisons()
    {
        return comparisons;
    }

    public int getMovements()
    {
        return movements;
    }

    public long getTime()
    {
        return time;
    }

    protected void sort(int ai[])
    {
    }

    private void clearStats()
    {
        comparisons = 0;
        movements = 0;
        time = 0L;
    }

    protected int comparisons;
    protected int movements;
    protected long time;
    protected static final SortingExperiment selectionSort = new SortingExperiment() {

        protected void sort(int list[])
        {
            for(int j = list.length - 1; j > 0; j--)
            {
                int maxpos = 0;
                for(int k = 1; k <= j; k++)
                {
                    comparisons++;
                    if(list[k] > list[maxpos])
                        maxpos = k;
                }

                if(j != maxpos)
                {
                    movements += 3;
                    int temp = list[j];
                    list[j] = list[maxpos];
                    list[maxpos] = temp;
                }
            }

        }

    }
;
    protected static final SortingExperiment insertionSort = new SortingExperiment() {

        protected void sort(int list[])
        {
            for(int j = 1; j < list.length; j++)
            {
                movements++;
                int temp = list[j];
                int k;
                for(k = j; k > 0 && list[k - 1] > temp; k--)
                {
                    comparisons++;
                    movements++;
                    list[k] = list[k - 1];
                }

                if(k > 0)
                    comparisons++;
                movements++;
                list[k] = temp;
            }

        }

    }
;
    protected static final SortingExperiment bubbleSort = new SortingExperiment() {

        protected void sort(int list[])
        {
            int temp;
            do
            {
                movements++;
                temp = list[0];
                for(int j = 1; j < list.length; j++)
                {
                    comparisons++;
                    if(list[j - 1] > list[j])
                    {
                        movements += 3;
                        temp = list[j];
                        list[j] = list[j - 1];
                        list[j - 1] = temp;
                    }
                }

                comparisons++;
            } while(temp != list[0]);
        }

    }
;
    protected static final SortingExperiment shellSort = new SortingExperiment() {

        protected void sort(int list[])
        {
            int h = 1;
            do
                h = 3 * h + 1;
            while(h <= list.length);
            do
            {
                h /= 3;
                for(int j = h; j < list.length; j++)
                {
                    movements++;
                    int temp = list[j];
                    int k = j;
                    for(comparisons++; list[k - h] > temp; comparisons++)
                    {
                        movements++;
                        list[k] = list[k - h];
                        k -= h;
                        if(k < h)
                            break;
                    }

                    movements++;
                    list[k] = temp;
                }

            } while(h != 1);
        }

    }
;
    protected static final SortingExperiment quickSort = new SortingExperiment() {

        protected void sort(int list[])
        {
            qsort(list, 0, list.length - 1);
        }

        private void qsort(int list[], int low, int high)
        {
            if(low < high)
            {
                int pivot = partition(list, low, high);
                qsort(list, low, pivot);
                qsort(list, pivot + 1, high);
            }
        }

        private int partition(int list[], int low, int high)
        {
            movements++;
            int temp = list[low];
            int i = low - 1;
            int j = high + 1;
            do
            {
                do
                {
                    j--;
                    comparisons++;
                } while(list[j] > temp);
                do
                {
                    i++;
                    comparisons++;
                } while(list[i] < temp);
                if(i < j)
                {
                    movements += 3;
                    int swapTemp = list[i];
                    list[i] = list[j];
                    list[j] = swapTemp;
                } else
                {
                    return j;
                }
            } while(true);
        }

    }
;
    protected static final SortingExperiment mergeSort = new SortingExperiment() {

        protected void sort(int list[])
        {
            msort(list, 0, list.length - 1);
        }

        private void msort(int list[], int low, int high)
        {
            if(low < high)
            {
                int mid = (low + high) / 2;
                msort(list, low, mid);
                msort(list, mid + 1, high);
                merge(list, low, mid, high);
            }
        }

        private void merge(int list[], int low, int mid, int high)
        {
            int h = low;
            int i = 0;
            int j = mid + 1;
            int otherList[] = new int[(high - low) + 1];
            while(h <= mid && j <= high) 
            {
                comparisons++;
                if(list[h] <= list[j])
                {
                    movements++;
                    otherList[i] = list[h];
                    h++;
                } else
                {
                    movements++;
                    otherList[i] = list[j];
                    j++;
                }
                i++;
            }
            if(h > mid)
            {
                for(int k = j; k <= high; k++)
                {
                    movements++;
                    otherList[i] = list[k];
                    i++;
                }

            } else
            {
                for(int k = h; k <= mid; k++)
                {
                    movements++;
                    otherList[i] = list[k];
                    i++;
                }

            }
            for(int m = 0; m < otherList.length; m++)
            {
                movements++;
                list[low + m] = otherList[m];
            }

        }

    }
;
    protected static final SortingExperiment heapSort = new SortingExperiment() {

        protected void sort(int list[])
        {
            int n = list.length;
            for(int k = n / 2; k >= 0; k--)
                downheap(list, k, n);

            for(int j = n - 1; j > 0; j--)
            {
                movements += 3;
                int temp = list[0];
                list[0] = list[j];
                list[j] = temp;
                downheap(list, 0, j);
            }

        }

        private void downheap(int list[], int k, int maxPos)
        {
            movements++;
            int temp = list[k];
            int maxChildIndex;
            for(; k < maxPos / 2; k = maxChildIndex)
            {
                maxChildIndex = 2 * k + 1;
                if(maxChildIndex < maxPos - 1)
                {
                    comparisons++;
                    if(list[maxChildIndex] < list[maxChildIndex + 1])
                        maxChildIndex++;
                }
                comparisons++;
                if(temp >= list[maxChildIndex])
                    break;
                movements++;
                list[k] = list[maxChildIndex];
            }

            movements++;
            list[k] = temp;
        }

    }
;

}
