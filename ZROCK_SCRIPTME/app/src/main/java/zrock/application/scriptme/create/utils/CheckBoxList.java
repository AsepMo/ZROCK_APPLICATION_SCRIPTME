package zrock.application.scriptme.create.utils;

import zrock.application.scriptme.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

/**
 * This class represents a list CheckTextViews
 * 
 * NOTE: Make sure the type being provided has a pretty toString()
 * implementation!
 */
public class CheckBoxList<T> {
   private final List<T> m_list = new ArrayList<T>();

   private final ListView m_listView;

   private Activity m_parentActivity;

   private ArrayAdapter<T> m_adapter;

   private final Comparator<T> m_comparator = new Comparator<T>() {
      /**
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      @Override
      public int compare(T l, T r) {
         return l.toString().compareToIgnoreCase(r.toString());
      }
   };

   /**
    * Constructor
    * 
    * @param activity
    */
   public CheckBoxList(Activity activity) {
      m_parentActivity = activity;

      // Set up list adapter
      m_listView = (ListView) m_parentActivity.findViewById(R.id.list);
      m_adapter = new ArrayAdapter<T>(m_parentActivity,
            R.layout.list_item_checked, m_list);
      m_listView.setAdapter(m_adapter);

      // Set up choice mode
      m_listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

      // Set OnItemClickListener
      m_listView.setOnItemClickListener(new OnItemClickListener() {
         /**
          * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView,
          *      android.view.View, int, long)
          */
         @Override
         public void onItemClick(AdapterView<?> parent, View view,
               int position, long id) {
            // Removes the selected CheckTextView from the list of checked
            // items. Have to check if isChecked() since the CheckedTextView's
            // state hasn't been changed at this point.
            if (((CheckedTextView) view).isChecked()) {
               // I'm not sure why I've got to handle this, as opposed to the
               // API doing it internally.
               m_listView.getCheckedItemPositions().delete(position);
            }

            if (m_listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE) {
               setSelected(position, true);
            }

            // This is wicked cool!
            try {
               Method m = m_parentActivity.getClass().getDeclaredMethod(
                     "listItemClick", int.class);
               m.invoke(m_parentActivity, position);
            } catch (SecurityException e) {
               e.printStackTrace();
            } catch (IllegalArgumentException e) {
               e.printStackTrace();
            } catch (IllegalAccessException e) {
               e.printStackTrace();
            } catch (InvocationTargetException e) {
               e.printStackTrace();
            } catch (NoSuchMethodException e) {
               // Ignore in case user didn't want to define it
            }
         }
      });

      // Set OnItemLongClickListener
      m_listView.setOnItemLongClickListener(new OnItemLongClickListener() {
         /**
          * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView,
          *      android.view.View, int, long)
          */
         @Override
         public boolean onItemLongClick(AdapterView<?> parent, View view,
               int position, long id) {
            if (m_listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE) {
               setSelected(position, true);
            }

            // This is wicked cool!
            try {
               Method m = m_parentActivity.getClass().getDeclaredMethod(
                     "listItemLongClick", int.class);
               m.invoke(m_parentActivity, position);
            } catch (SecurityException e) {
               e.printStackTrace();
            } catch (IllegalArgumentException e) {
               e.printStackTrace();
            } catch (IllegalAccessException e) {
               e.printStackTrace();
            } catch (InvocationTargetException e) {
               e.printStackTrace();
            } catch (NoSuchMethodException e) {
               // Ignore in case user didn't want to define it
            }
            return true;
         }
      });
   }

   /**
    * Add an item to the list
    * 
    * @param item
    * @return This list
    */
   public CheckBoxList<T> add(T item) {
      m_list.add(item);
      Collections.sort(m_list, m_comparator);
      setSelected(item, false);
      return this;
   }

   /**
    * Add all items from a list to this list
    * 
    * @param items
    * @return This list
    */
   public CheckBoxList<T> addAll(List<T> items) {
      m_list.addAll(items);
      Collections.sort(m_list, m_comparator);
      ((BaseAdapter) m_listView.getAdapter()).notifyDataSetChanged();
      return this;
   }

   /**
    * Does the list contain this item
    * 
    * @param item
    * @return True if list contains item, false otherwise
    */
   public boolean contains(T item) {
      return m_list.contains(item);
   }

   /**
    * Get the item at the specified position
    * 
    * @param position
    * @return The item specified
    */
   public T getItem(int position) {
      try {
         return m_list.get(position);
      } catch (IndexOutOfBoundsException ioobe) {
         return null;
      }
   }

   /**
    * Get all the selected items
    * 
    * @return The selected items
    */
   public List<T> getSelectedItems() {
      List<T> selected = new ArrayList<T>();
      SparseBooleanArray sba = m_listView.getCheckedItemPositions();
      for (int i = 0; i < sba.size(); ++i) {
         selected.add(m_list.get(sba.keyAt(i)));
      }
      return selected;
   }

   /**
    * Hide the list
    * 
    * @return This list
    */
   public CheckBoxList<T> hide() {
      m_listView.setVisibility(View.INVISIBLE);
      return this;
   }

   /**
    * Is the list empty
    * 
    * @return True if the list is empty, false otherwise
    */
   public boolean isEmpty() {
      return m_list.isEmpty();
   }

   /**
    * Refresh the list view
    * 
    * @return This list
    */
   public CheckBoxList<T> refresh() {
      ((BaseAdapter) m_listView.getAdapter()).notifyDataSetChanged();
      return this;
   }

   /**
    * Remove the specified items
    * 
    * @param items
    * @return True if all the items were removed, false otherwise
    */
   public boolean remove(List<T> items) {
      List<T> selected = getSelectedItems();
      m_listView.clearChoices();
      boolean toRet = true;
      for (T item : items) {
         toRet = (toRet & m_list.remove(item)) ? true : false;
         selected.remove(item);
         for (T t : selected) {
            for (int i = 0; i < m_list.size(); ++i) {
               if (m_list.get(i) == t) {
                  m_listView.setItemChecked(i, true);
                  break;
               }
            }
         }
      }
      ((BaseAdapter) m_listView.getAdapter()).notifyDataSetChanged();
      return toRet;
   }

   /**
    * Remove the specified item
    * 
    * @param item
    * @return True if the item was removed, false otherwise
    */
   public boolean remove(T item) {
      List<T> selected = getSelectedItems();
      m_listView.clearChoices();
      boolean toRet = m_list.remove(item);
      selected.remove(item);
      for (T t : selected) {
         for (int i = 0; i < m_list.size(); ++i) {
            if (m_list.get(i) == t) {
               m_listView.setItemChecked(i, true);
               break;
            }
         }
      }
      ((BaseAdapter) m_listView.getAdapter()).notifyDataSetChanged();
      return toRet;
   }

   /**
    * Remove all items from the list
    * 
    * @return This list
    */
   public CheckBoxList<T> removeAll() {
      m_list.clear();
      ((BaseAdapter) m_listView.getAdapter()).notifyDataSetChanged();
      return this;
   }

   /**
    * Remove the selected items from the list
    * 
    * @return This list
    */
   public CheckBoxList<T> removeSelected() {
      SparseBooleanArray sba = m_listView.getCheckedItemPositions();
      List<Integer> selectedIndices = new ArrayList<Integer>();
      for (int i = 0; i < sba.size(); ++i) {
         selectedIndices.add(sba.keyAt(i));
      }

      Collections.sort(selectedIndices);
      for (int i = selectedIndices.size() - 1; i >= 0; --i) {
         m_list.remove(m_list.get(selectedIndices.get(i)));
      }

      m_listView.clearChoices();
      ((BaseAdapter) m_listView.getAdapter()).notifyDataSetChanged();
      return this;
   }

   /**
    * Select all items in the list
    * 
    * @return This list
    */
   public CheckBoxList<T> selectAll() {
      for (int i = 0; i < m_listView.getCount(); ++i) {
         m_listView.setItemChecked(i, true);
      }
      ((BaseAdapter) m_listView.getAdapter()).notifyDataSetChanged();
      return this;
   }

   /**
    * Set all items in list as not selected
    * 
    * @return This list
    */
   public CheckBoxList<T> selectNone() {
      m_listView.clearChoices();
      // Removes the selected CheckTextView from the list of checked items
      // I'm not sure why I've got to handle this, as opposed to the API
      // doing it internally.
      m_listView.getCheckedItemPositions().clear();
      ((BaseAdapter) m_listView.getAdapter()).notifyDataSetChanged();
      return this;
   }

   /**
    * Set the choice mode for the list view
    * 
    * @param choice
    * @return This list
    */
   public CheckBoxList<T> setChoiceMode(int choice) {
      m_listView.setChoiceMode(choice);
      return this;
   }

   /**
    * Set the item at position to be selected if select is true, not selected
    * otherwise
    * 
    * @param position
    * @param select
    * @return This list
    */
   public CheckBoxList<T> setSelected(int position, boolean select) {
      m_listView.setItemChecked(position, select);
      // Removes the selected CheckTextView from the list of checked items
      if (!select) {
         // I'm not sure why I've got to handle this, as opposed to the API
         // doing it internally.
         m_listView.getCheckedItemPositions().delete(position);
      }
      ((BaseAdapter) m_listView.getAdapter()).notifyDataSetChanged();
      return this;
   }

   /**
    * Set the item to be selected if select is true, not selected otherwise
    * 
    * @param item
    * @param select
    * @return This list
    */
   public CheckBoxList<T> setSelected(T item, boolean select) {
      for (int i = 0; i < m_list.size(); ++i) {
         if (m_list.get(i) == item) {
            m_listView.setItemChecked(i, select);
            // Removes the selected CheckTextView from the list of checked items
            if (!select) {
               // I'm not sure why I've got to handle this, as opposed to the
               // API
               // doing it internally.
               m_listView.getCheckedItemPositions().delete(i);
            }
            ((BaseAdapter) m_listView.getAdapter()).notifyDataSetChanged();
            break;
         }
      }
      return this;
   }

   /**
    * Show the list
    * 
    * @return This list
    */
   public CheckBoxList<T> show() {
      m_listView.setVisibility(View.VISIBLE);
      return this;
   }

   /**
    * Set the checkboxes to be visible if show is true, invisible otherwise
    * 
    * @param show
    * @return This list
    */
   public CheckBoxList<T> showCheckBox(boolean show) {
      if (show) {
         m_adapter = new ArrayAdapter<T>(m_parentActivity,
               R.layout.list_item_checked, m_list);
      } else {
         m_adapter = new ArrayAdapter<T>(m_parentActivity, R.layout.list_item,
               m_list);
      }

      m_listView.setAdapter(m_adapter);

      return this;
   }
}
