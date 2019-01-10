const char *
decode_enumerated_bitfield_shifted(guint32 val, guint32 mask, int width,
    const value_string *tab, const char *fmt)
{
  static char buf[1025];
  char *p;
  int shift = 0;

  /* Compute the number of bits we have to shift the bitfield right
     to extract its value. */
  while ((mask & (1<<shift)) == 0)
    shift++;

  p = decode_bitfield_value(buf, val, mask, width);
  g_snprintf(p, (gulong) (1024-(p-buf)), fmt, val_to_str((val & mask) >> shift, tab, "Unknown"));
  return buf;
}
