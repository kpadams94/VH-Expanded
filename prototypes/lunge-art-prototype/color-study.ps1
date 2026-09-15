$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.Drawing
$root=$PSScriptRoot
$mask=[Drawing.Bitmap]::new((Join-Path $root 'native/03-silhouette.png'))
$im=[Drawing.Bitmap]::new(16,16,[Drawing.Imaging.PixelFormat]::Format32bppArgb)
$outline=[Drawing.ColorTranslator]::FromHtml('#4B2519')
for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){
 if($mask.GetPixel($x,$y).A -eq 0){continue}
 foreach($d in @(@(-1,0),@(1,0),@(0,-1),@(0,1))){$xx=$x+$d[0];$yy=$y+$d[1];if($xx -ge 0 -and $xx -lt 16 -and $yy -ge 0 -and $yy -lt 16){$im.SetPixel($xx,$yy,$outline)}}
}}
for($y=0;$y -lt 16;$y++){
 $xs=@();for($x=0;$x -lt 16;$x++){if($mask.GetPixel($x,$y).A -gt 0){$xs+=$x}}
 foreach($x in $xs){
  $color='#FFCF54'
  if($y -le 10){if($x -eq $xs[0]){$color='#FFF2BC'}elseif($x -eq $xs[-1]){$color='#EB8B32'}}
  elseif($y -eq 11){$color='#EB8B32'}
  elseif($y -eq 12){$color='#FFDF79'}
  elseif($y -eq 13){$color='#F5A53B'}
  elseif($y -eq 14){$color='#D86B27'}
  $im.SetPixel($x,$y,[Drawing.ColorTranslator]::FromHtml($color))
 }
}
$im.Save((Join-Path $root 'native/03-color-draft.png'),[Drawing.Imaging.ImageFormat]::Png)
$preview=[Drawing.Bitmap]::new(320,320);$g=[Drawing.Graphics]::FromImage($preview);$g.Clear([Drawing.ColorTranslator]::FromHtml('#191C22'));$g.InterpolationMode=[Drawing.Drawing2D.InterpolationMode]::NearestNeighbor;$g.PixelOffsetMode=[Drawing.Drawing2D.PixelOffsetMode]::Half;$g.DrawImage($im,[Drawing.Rectangle]::new(0,0,320,320),0,0,16,16,[Drawing.GraphicsUnit]::Pixel);$g.Dispose();$preview.Save((Join-Path $root 'color-draft-preview.png'),[Drawing.Imaging.ImageFormat]::Png);$im.Dispose();$mask.Dispose();$preview.Dispose()
